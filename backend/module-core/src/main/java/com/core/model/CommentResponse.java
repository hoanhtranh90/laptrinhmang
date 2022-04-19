package com.core.model;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {
    private Long id;
    private String body;
    private Long post_id;
    private Long createdBy;
    private Date createdAt;
    private UserSummary userSummary;
}
