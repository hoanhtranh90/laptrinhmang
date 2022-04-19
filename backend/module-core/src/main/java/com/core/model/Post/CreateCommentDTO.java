package com.core.model.Post;

import lombok.Data;

@Data
public class CreateCommentDTO {
    private String content;
    private Long postId;
}
