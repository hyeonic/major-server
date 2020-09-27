package com.majorrunner.majorserver.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /** comment 수정 */
    @Transactional
    public void updateComment(Comment updateComment, Comment updatedComment) { // updated에 있는 내용을 update에 넣어준다.

        // comment값 수정
        updateComment.setComment(updatedComment.getComment());

        updateComment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(updateComment);
    }
}
