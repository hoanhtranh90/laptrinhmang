package com.core.mapper;

import com.core.entity.Comment;
import com.core.model.CommentResponse;
import com.core.model.UserSummary;
import com.core.model.account.UserRegisterDto;

import com.core.repository.UserRepository;

import com.core.entity.User;

import com.core.utils.Constants;
import com.core.utils.DateUtils;
import com.core.utils.H;
import com.core.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class MapperObject {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    private MapperObject() {

    }

    public static User userBuilder(UserRegisterDto userDto) {
        return User.builder().email(userDto.getEmail())
                .fullName(userDto.getFullName())
                .sex(userDto.getSex())
                .phoneNumber(userDto.getPhoneNumber())
                .userName(userDto.getUserName())
                .dateOfBirth(userDto.getDateOfBirth())
                .address(userDto.getAddress()).build();
    }
    public static CommentResponse mapCommentToCommentResponse(Comment comment, User author) {
        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setId(comment.getId());
        commentResponse.setBody(comment.getBody());
        commentResponse.setCreatedAt(comment.getCreatedDate());
        commentResponse.setPost_id(comment.getPost().getId());
        commentResponse.setCreatedBy(author.getUserId());

        UserSummary userSummary = new UserSummary();
        userSummary.setId(author.getUserId());
        userSummary.setEmail(author.getEmail());
        userSummary.setUsername(author.getUserName());
        userSummary.setFullName(author.getFullName());
        userSummary.setPhoneNumber(author.getPhoneNumber());
        commentResponse.setUserSummary(userSummary);
        return commentResponse;
    }

}
