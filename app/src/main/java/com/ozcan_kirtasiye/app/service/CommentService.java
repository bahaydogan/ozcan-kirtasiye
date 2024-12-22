package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.*;
import com.ozcan_kirtasiye.app.repository.ICommentRepo;
import com.ozcan_kirtasiye.app.repository.IProductRepo;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
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

    public Comment addComment(User user, Long productId, String text) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setProduct(product);
        comment.setCommentText(text);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByProduct(Long productId) {
        return commentRepository.findByProductId(productId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

