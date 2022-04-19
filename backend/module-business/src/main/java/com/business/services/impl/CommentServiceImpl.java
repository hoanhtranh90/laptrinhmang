package com.business.services.impl;

import com.business.services.CommentService;
import com.business.utilts.ApplicationUtils;
import com.core.entity.Comment;
import com.core.exception.BadRequestException;
import com.core.model.Post.CreateCommentDTO;
import com.core.repository.Post.CommentRepository;
import com.core.repository.Post.PostRepository;
import com.core.utils.Constants;
import com.core.utils.H;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public Comment addComment(CreateCommentDTO createCommentDTO) {
        Comment comment = new Comment();
        comment.setBody(createCommentDTO.getContent());
        comment.setPost(postRepository.findById(createCommentDTO.getPostId()).get());
        comment.setUser(ApplicationUtils.getCurrentUser());
        return commentRepository.save(comment);
    }

    @Override
    public Comment deleteComment(Long commentId) throws BadRequestException {
        Comment comment = commentRepository.findById(commentId).get();
        if(!H.isTrue(comment)) throw new BadRequestException("Comment not found");
        comment.setIsDelete(Constants.DELETE.DELETED);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentByPostId(Long postId) {
        return commentRepository.findByPostIdAndIsDelete(postId, Constants.DELETE.NORMAL);
    }
}
