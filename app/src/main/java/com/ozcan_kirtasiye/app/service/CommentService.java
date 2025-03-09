package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.*;
import com.ozcan_kirtasiye.app.repository.ICommentRepo;
import com.ozcan_kirtasiye.app.repository.IProductRepo;
import com.ozcan_kirtasiye.app.security.CurrentUser;
import jakarta.transaction.Transactional;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private ICommentRepo commentRepository;

    @Autowired
    private IProductRepo productRepository;

    @Autowired
    private IUserService userService;

    public Comment addComment(CurrentUser currentUser , Long productId, String text) {

        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        User user = userService.getUserById(currentUser.getId());

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setProduct(product);
        comment.setCommentText(text);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Transactional
    public List<Comment> getCommentsByProduct(Long productId) {
        return commentRepository.findByProductId(productId);
    }

    public List<Comment> getCommentsByUserId(Long userId) {
        // Fetch and return comments for the user
        return commentRepository.findByUserId(userId);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }


}

