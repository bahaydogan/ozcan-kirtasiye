package com.ozcan_kirtasiye.app.repository;


import com.ozcan_kirtasiye.app.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByProductId(Long productId);
    Optional<Comment> findById(Long commentId);
    List<Comment> findByUserId(Long userId);
}