package com.majorrunner.majorserver.comment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/comment", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class CommentController {

    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @PutMapping("/{id}")
    public ResponseEntity updateComment(@PathVariable Long id,
                                        @RequestBody CommentDto.UpdateCommentRequest updateCommentRequest) {

        Comment updatedComment = modelMapper.map(updateCommentRequest, Comment.class);
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (!optionalComment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Comment comment = optionalComment.get();
        commentService.updateComment(comment, updatedComment);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id) {

        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (!optionalComment.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Comment comment = optionalComment.get();
        commentRepository.delete(comment);

        return ResponseEntity.noContent().build();
    }
}
