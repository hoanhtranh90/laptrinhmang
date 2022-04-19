package com.business.services;

import com.core.entity.Comment;
import com.core.exception.BadRequestException;
import com.core.model.Post.CommentResponseDTO;
import com.core.model.Post.CreateCommentDTO;

import java.util.List;

public interface CommentService {
    Comment addComment(CreateCommentDTO createCommentDTO);
    Comment deleteComment(Long commentId) throws BadRequestException;

    List<CommentResponseDTO> getCommentByPostId(Long postId);
}
