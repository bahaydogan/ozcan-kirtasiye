package com.ozcan_kirtasiye.app.dto;

import com.ozcan_kirtasiye.app.model.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String commentText;
    private LocalDateTime createdAt;
    private String username;

    public static CommentDTO from(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setCommentText(comment.getCommentText());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUsername(comment.getUser().getName());
        return dto;
    }

    //todo: commentdto yu controlleda aktif şekilde kullan.
    // ...
}

