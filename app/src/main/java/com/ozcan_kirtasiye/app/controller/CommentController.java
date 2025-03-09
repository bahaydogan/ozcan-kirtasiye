package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.dto.CommentDTO;
import com.ozcan_kirtasiye.app.model.Comment;
import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.security.CurrentUser;
import com.ozcan_kirtasiye.app.service.CommentService;
import com.ozcan_kirtasiye.app.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping //localhost:8080/api/comments?productId=2&text=yorumyorumyorum
    public ResponseEntity<?> addComment_new(@AuthenticationPrincipal CurrentUser currentUser,
                                                 @RequestParam Long productId,
                                                 @RequestParam String text) {
        //Comment comment = commentService.addComment(loggedUser, productId, text);
        //return ResponseEntity.ok(CommentDTO.from(comment));
        return new ResponseEntity<>(commentService.addComment(currentUser, productId, text),HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentDTO>> getCommentsForProduct(@PathVariable Long productId) {
        List<Comment> comments = commentService.getCommentsByProduct(productId);
        return ResponseEntity.ok(comments.stream().map(CommentDTO::from).collect(Collectors.toList()));
    }


    @DeleteMapping("/admin/{commentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment2(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable Long commentId) {
        if (commentService.getCommentsByUserId(currentUser.getId()).contains(commentService.getCommentById(commentId).get())) {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok("Comment deleted");
        }
        else return ResponseEntity.notFound().build();
    }


    @GetMapping("/{userId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByUser(@PathVariable Long userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

}

