package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.dto.CommentDTO;
import com.ozcan_kirtasiye.app.model.Comment;
import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@AuthenticationPrincipal User user,
                                                 @RequestParam Long productId,
                                                 @RequestParam String text) {
        Comment comment = commentService.addComment(user, productId, text);
        return ResponseEntity.ok(CommentDTO.from(comment));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentDTO>> getCommentsForProduct(@PathVariable Long productId) {
        List<Comment> comments = commentService.getCommentsByProduct(productId);
        return ResponseEntity.ok(comments.stream().map(CommentDTO::from).collect(Collectors.toList()));
    }


    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

