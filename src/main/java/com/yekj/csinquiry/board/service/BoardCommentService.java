package com.yekj.csinquiry.board.service;

import com.yekj.csinquiry.board.dto.BoardCommentFormDTO;
import com.yekj.csinquiry.board.entity.BoardComment;
import com.yekj.csinquiry.board.repository.BoardCommentRepository;
import com.yekj.csinquiry.config.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardCommentService {
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Transactional
    public void addComment(String token, BoardCommentFormDTO comment) throws Exception{
        Long uid = jwtProvider.getSubject(token);
        BoardComment newComment = new BoardComment();

        newComment.setBoardId(Long.valueOf(comment.getBoardId()));
        newComment.setComment(comment.getComment());
        newComment.setWriter(uid);

        boardCommentRepository.save(newComment);
    }

}
