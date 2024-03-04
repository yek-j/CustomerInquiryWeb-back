package com.yekj.csinquiry.board.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yekj.csinquiry.board.dto.BoardDTO;
import com.yekj.csinquiry.board.dto.BoardFormDTO;
import com.yekj.csinquiry.board.dto.BoardListDTO;
import com.yekj.csinquiry.board.dto.BoardPageDTO;
import com.yekj.csinquiry.board.entity.Board;
import com.yekj.csinquiry.board.entity.BoardComment;
import com.yekj.csinquiry.board.entity.QBoard;
import com.yekj.csinquiry.board.repository.BoardCommentRepository;
import com.yekj.csinquiry.board.repository.BoardRepository;
import com.yekj.csinquiry.config.JwtProvider;
import com.yekj.csinquiry.user.entity.QGroup;
import com.yekj.csinquiry.user.entity.QUser;
import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardService {
    @Autowired
    private JwtProvider jwtProvider;

    private final JPAQueryFactory queryFactory;

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

    public BoardPageDTO getBoardList(String token, boolean admin, int page) {
        Long groupId = jwtProvider.getGroup(token);

        QBoard qBoard  = QBoard.board;

        JPAQuery<Tuple> query = queryFactory
                .select(qBoard.id, qBoard.title, qBoard.content,
                        qBoard.writer.name, qBoard.writer.group.name, qBoard.wdate, qBoard.resolved)
                .from(qBoard);


        if(!admin) query.where(qBoard.writer.group.id.eq(groupId));
        query.offset(page * 10L);
        query.limit(10);
        List<Tuple> result = query.fetch();

        List<BoardListDTO> boardList = result.stream()
                .map(t -> {
                    Long id = t.get(0, Long.class);
                    String title = t.get(1, String.class);
                    String content = t.get(2, String.class);
                    String writer = t.get(3, String.class);
                    String group = t.get(4, String.class);
                    Date wdate = t.get(5, Date.class);
                    boolean resolved = Boolean.TRUE.equals(t.get(6, Boolean.class));

                    if (content.length() > 100) {
                        content = content.substring(0, 100);
                    }
                    return new BoardListDTO(id, writer, title, content, group, wdate.toString(), resolved);
                })
                .collect(Collectors.toList());

        Long pageCount = 1L;
        if(admin) pageCount = pageCount(null);
        else pageCount = pageCount(Long.valueOf(boardList.size()));

        BoardPageDTO resultPage = new BoardPageDTO(pageCount, boardList);

        return resultPage;
    }

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

            List<BoardComment> boardCommentList = boardCommentRepository.findBoardCommentByBoardId(id);

            System.out.println(boardCommentList.size());

            boardDTO.setWriterName(board.get().getWriter().getName());
            boardDTO.setGroupName(board.get().getWriter().getGroup().getName());

        } else {
            throw new Exception("게시글을 찾을 수 없습니다.");
        }

        return boardDTO;
    }

    public void deleteBoard(Long id, String token) throws Exception{
        Long userId = jwtProvider.getSubject(token);

        Optional<Board> board = boardRepository.findById(id);

        if (board.isPresent()) {
            Long boardWriterId = board.get().getWriter().getId();
            if (!Objects.equals(userId, boardWriterId)) throw new Exception("권한이 없는 게시물은 삭제할 수 없습니다.");

            boardRepository.delete(board.get());

        } else {
            throw new Exception("존재하지 않는 게시판으로 삭제할 수 없습니다.");
        }
    }

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
