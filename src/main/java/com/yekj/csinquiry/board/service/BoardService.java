package com.yekj.csinquiry.board.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yekj.csinquiry.board.dto.BoardListDTO;
import com.yekj.csinquiry.board.dto.BoardPageDTO;
import com.yekj.csinquiry.board.entity.Board;
import com.yekj.csinquiry.board.entity.QBoard;
import com.yekj.csinquiry.board.repository.BoardRepository;
import com.yekj.csinquiry.config.JwtProvider;
import com.yekj.csinquiry.user.entity.QGroup;
import com.yekj.csinquiry.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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
    public BoardService(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public BoardPageDTO getBoardList(String token, boolean admin, int page) {
        Long groupId = jwtProvider.getGroup(token);

        QBoard qBoard  = QBoard.board;
        QUser qUser = QUser.user;
        QGroup qGroup = QGroup.group;

        JPAQuery<Tuple> query = queryFactory
                .select(qBoard.id, qBoard.title, qBoard.content, qUser.name, qGroup.name, qBoard.wdate)
                .from(qBoard)
                .join(qUser).on(qBoard.writer.eq(qUser.id))
                .join(qGroup).on(qBoard.gid.eq(qGroup.id));


        if(!admin) query.where(qBoard.gid.eq(groupId));
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

                    if (content.length() > 100) {
                        content = content.substring(0, 100);
                    }
                    return new BoardListDTO(id, writer, title, content, group, wdate.toString());
                })
                .collect(Collectors.toList());

        BoardPageDTO resultPage = new BoardPageDTO(pageCount(), boardList);

        return resultPage;
    }

    public void addBoard(String title, String content, String token) throws Exception{
        Long uid = jwtProvider.getSubject(token);
        Long gid = jwtProvider.getGroup(token);

        Board newBoard = new Board();
        newBoard.setTitle(title);
        newBoard.setContent(content);
        newBoard.setWriter(uid);
        newBoard.setGid(gid);

        boardRepository.save(newBoard);
    }

    public boolean validBoard(String title, String content) {
        if(title == null || content == null) return false;
        if(title.isEmpty() || content.isEmpty()) return false;

        return true;
    }

    private long pageCount() {
        long count = boardRepository.count();
        long total = count / 10;
        if (count % 10 > 0) {
            total++;
        }
        return total;
    }
}
