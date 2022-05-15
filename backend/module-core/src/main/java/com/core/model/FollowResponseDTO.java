package com.core.model;

import com.core.entity.User;
import com.core.model.user.UserDTO;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class FollowResponseDTO {
    private UserDTO user;

    private Long isFollow;
}
