package com.business.services.impl;

import com.business.services.AcountManagementService;
import com.business.services.FollowService;
import com.business.utilts.ApplicationUtils;
import com.business.utilts.H;
import com.core.entity.Follow;
import com.core.entity.User;
import com.core.model.AddFollow;
import com.core.model.FollowResponseDTO;
import com.core.model.search.SearchFollowDTO;
import com.core.model.user.UserDTO;
import com.core.repository.FollowRepository;
import com.core.repository.UserRepository;
import com.core.utils.Constants;
import com.core.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AcountManagementService acountManagementService;


    @Override
    public UserDTO addFollow(AddFollow addFollow) {
        Follow follow = new Follow();
        if(addFollow.getIsFollow() == 1L) {
            follow.setFollower(ApplicationUtils.getCurrentUser());
            follow.setFollowing(acountManagementService.findByUserIdAndIsDelete(addFollow.getFollowId(), Constants.DELETE.NORMAL));
            followRepository.save(follow);
        }
        else {
            follow = followRepository.findByFollowerAndFollowing(ApplicationUtils.getCurrentUser().getUserId(), addFollow.getFollowId());
            if(H.isTrue(follow)) {
                followRepository.delete(follow);
            }
        }

        return modelMapper.map(follow.getFollowing(), UserDTO.class);
    }

}
