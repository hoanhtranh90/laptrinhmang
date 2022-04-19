package com.core.model.Post;

import com.core.entity.User;
import com.core.model.user.UserDTO;
import lombok.Data;

import java.util.Date;

@Data
public class CommentResponseDTO {

    private Date createdDate;
    private Date modifiedDate;
    private String body;
    private UserDTO user;
}
