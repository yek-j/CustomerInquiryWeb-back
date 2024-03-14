package com.yekj.csinquiry.board.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yekj.csinquiry.board.dto.*;
import com.yekj.csinquiry.board.entity.Board;
import com.yekj.csinquiry.board.entity.BoardComment;
import com.yekj.csinquiry.board.entity.QBoard;
import com.yekj.csinquiry.board.repository.BoardCommentRepository;
import com.yekj.csinquiry.board.repository.BoardRepository;
import com.yekj.csinquiry.config.JwtProvider;
import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardService {
    @Autowired
    private JwtProvider jwtProvider;

    private final JPAQueryFactory queryFactory;
    private final QBoard qBoard  = QBoard.board;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Autowired
    public BoardService(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public BoardPageDTO getBoardList(String token, boolean admin, int page, String resolvedFilter, String writerFilter) {
        Long groupId = jwtProvider.getGroup(token);


        JPAQuery<Tuple> query = queryFactory
                .select(qBoard.id, qBoard.title, qBoard.content,
                        qBoard.writer.name, qBoard.writer.group.name, qBoard.wdate, qBoard.resolved)
                .from(qBoard);

        BooleanBuilder builder = new BooleanBuilder();

        if(!admin) {
            builder.and(qBoard.writer.group.id.eq(groupId));
        }
        if(StringUtils.hasText(writerFilter)) {
            builder.and(qBoard.writer.name.eq(writerFilter));
        }

        if(StringUtils.hasText(resolvedFilter) && !resolvedFilter.equals("all")) {
            boolean whereResolved = resolvedFilter.equals("solved");
            builder.and(qBoard.resolved.eq(whereResolved));
        }

        Long total = queryFactory.select(qBoard.count()).from(qBoard).where(builder).fetchOne();

        List<Tuple> result = query.where(builder)
                .orderBy(qBoard.id.desc())
                .offset(page * 10L)
                .limit(10)
                .fetch();

        List<BoardListDTO> boardList = result.stream()
                .map(t -> {
                    Long id = t.get(0, Long.class);
                    String title = t.get(1, String.class);
                    String content = t.get(2, String.class);
                    String writer = t.get(3, String.class);
                    String group = t.get(4, String.class);
                    Date wdate = t.get(5, Date.class);
                    boolean resolved = Boolean.TRUE.equals(t.get(6, Boolean.class));

                    content = content.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
                    if(content.indexOf("<img") > -1) {
                        content = content.replaceAll("<img[^>]*>", "");
                    }
                    if (content.length() > 100) {
                        content = content.substring(0, 100);
                    }
                    return new BoardListDTO(id, writer, title, content, group, wdate.toString(), resolved);
                })
                .collect(Collectors.toList());

        long pageCount = pageCount(total);

        BoardPageDTO resultPage = new BoardPageDTO(pageCount, boardList);

        return resultPage;
    }

    @Transactional
    public void addBoard(String title, String content, String token) throws Exception{
        Long uid = jwtProvider.getSubject(token);
        Optional<User> user = userRepository.findById(uid);

        if(user.isPresent()) {
            Board newBoard = new Board();
            newBoard.setTitle(title);
            newBoard.setContent(content);
            newBoard.setWriter(user.get());

            boardRepository.save(newBoard);
        } else {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
    }

    public boolean validBoard(String title, String content) {
        if(title == null || content == null) return false;
        if(title.isEmpty() || content.isEmpty()) return false;

        return true;
    }

    public BoardDTO getBoard(Long id, String token, boolean admin) throws Exception {
        BoardDTO boardDTO = new BoardDTO();
        Long groupId = jwtProvider.getGroup(token);
        Long userId = jwtProvider.getSubject(token);

        Optional<Board> board = boardRepository.findById(id);

        if(board.isPresent()){
            if(!admin) {
                Long boardGroupID = board.get().getWriter().getGroup().getId();
                if (!Objects.equals(groupId, boardGroupID)) throw new Exception("권한이 없는 게시물은 확인할 수 없습니다.");
            }

            if(Objects.equals(userId, board.get().getWriter().getId())) boardDTO.setEdit(true);
            else boardDTO.setEdit(false);

            boardDTO.setTitle(board.get().getTitle());
            boardDTO.setContent(board.get().getContent());
            boardDTO.setWdate(board.get().getWdate());
            boardDTO.setResolved(board.get().isResolved());
            boardDTO.setWriterName(board.get().getWriter().getName());
            boardDTO.setGroupName(board.get().getWriter().getGroup().getName());
        } else {
            throw new Exception("게시글을 찾을 수 없습니다.");
        }

        return boardDTO;
    }

    @Transactional
    public void deleteBoard(Long id, String token) throws Exception{
        Long userId = jwtProvider.getSubject(token);

        Optional<Board> board = boardRepository.findById(id);

        if (board.isPresent()) {
            Long boardWriterId = board.get().getWriter().getId();
            if (!Objects.equals(userId, boardWriterId)) throw new Exception("권한이 없는 게시물은 삭제할 수 없습니다.");

            List<BoardComment> deleteComment = boardCommentRepository.findBoardCommentByBoardId(id);
            if(!deleteComment.isEmpty()) boardCommentRepository.deleteAll(deleteComment);
            boardRepository.delete(board.get());

        } else {
            throw new Exception("존재하지 않는 게시판으로 삭제할 수 없습니다.");
        }
    }

    @Transactional
    public void updateBoard(Long id, String token, BoardFormDTO updateBoard) throws Exception {
        Long userId = jwtProvider.getSubject(token);

        Optional<Board> board = boardRepository.findById(id);

        if(board.isPresent()) {
            Long boardWriterId = board.get().getWriter().getId();
            if (!Objects.equals(userId, boardWriterId)) throw new Exception("권한이 없는 게시물은 수정할 수 없습니다.");

            board.get().setTitle(updateBoard.getTitle());
            board.get().setContent(updateBoard.getContent());
        } else {
            throw new Exception("존재하지 않는 게시판으로 수정할 수 없습니다.");
        }
    }

    @Transactional
    public void updateResloved(Long id) throws Exception{
        Optional<Board> updateBoard = boardRepository.findById(id);
        if(updateBoard.isPresent()) {
            boolean old = updateBoard.get().isResolved();
            updateBoard.get().setResolved(!old);
        } else {
            throw new Exception("존재하지 않는 게시판으로 해결 상태 업데이트 실패");
        }
    }

    private long pageCount(Long count) {
        if(count == null) count = boardRepository.count();

        long total = count / 10;
        if ( count % 10 > 0) {
            total++;
        }
        return total;
    }

}
