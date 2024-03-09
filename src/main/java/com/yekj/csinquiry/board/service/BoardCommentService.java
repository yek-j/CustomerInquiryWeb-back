package com.yekj.csinquiry.board.service;

import com.yekj.csinquiry.board.dto.BoardCommentDTO;
import com.yekj.csinquiry.board.dto.BoardCommentFormDTO;
import com.yekj.csinquiry.board.entity.BoardComment;
import com.yekj.csinquiry.board.repository.BoardCommentRepository;
import com.yekj.csinquiry.config.JwtProvider;
import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BoardCommentService {
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private BoardCommentRepository boardCommentRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addComment(String token, BoardCommentFormDTO comment) throws Exception{
        Long uid = jwtProvider.getSubject(token);
        BoardComment newComment = new BoardComment();

        newComment.setBoardId(Long.valueOf(comment.getBoardId()));
        newComment.setComment(comment.getComment());

        Optional<User> user = userRepository.findById(uid);

        user.ifPresent(newComment::setWriter);

        boardCommentRepository.save(newComment);
    }

    public List<BoardCommentDTO> getCommentList(Long boardId) {
        List<BoardComment> getComment = boardCommentRepository.findBoardCommentByBoardIdOrderByIdAsc(boardId);
        List<BoardCommentDTO> boardCommentList = getComment.stream()
                .map(c -> new BoardCommentDTO(c.getId(), c.getWriter().getName(), c.getComment(), c.getWdate().toString())).toList();

        return boardCommentList;
    }

    public void deleteComment(Long commentId, String token, boolean admin) throws Exception {
        Long uid = jwtProvider.getSubject(token);
        Optional<BoardComment> deleteComment = boardCommentRepository.findById(commentId);
        if(deleteComment.isPresent()) {
            if(Objects.equals(uid, deleteComment.get().getWriter().getId()) || admin) {
                boardCommentRepository.delete(deleteComment.get());
            } else {
                throw new Exception("댓글은 작성자와 관리자만 삭제할 수 있습니다.");
            }
        } else {
            throw new Exception("삭제할 댓글이 존재하지 않습니다.");
        }
    }

    public void updateComment(String comment, Long commentId, String token) throws Exception {
        Long uid = jwtProvider.getSubject(token);
        Optional<BoardComment> updateComment = boardCommentRepository.findById(commentId);

        if(updateComment.isPresent()) {
            if(Objects.equals(uid, updateComment.get().getWriter().getId()) ) {
                updateComment.get().setComment(comment);
            } else {
                throw new Exception("댓글은 작성자만 수정할 수 있습니다.");
            }
        } else {
            throw new Exception("수정할 댓글이 존재하지 않습니다.");
        }
    }

}
