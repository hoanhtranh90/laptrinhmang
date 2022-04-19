package com.business.mapper;

import com.core.entity.Comment;
import com.core.model.Post.CommentResponseDTO;
import com.core.model.user.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapper {
    @Autowired
    private ModelMapper modelMapper;
    public CommentResponseDTO maptoCommentResponseDTO(Comment comment) {
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setCreatedDate(comment.getCreatedDate());
        commentResponseDTO.setModifiedDate(comment.getModifiedDate());
        commentResponseDTO.setBody(comment.getBody());
        commentResponseDTO.setUser(modelMapper.map(comment.getUser(), UserDTO.class));
        return commentResponseDTO;
    }
}
