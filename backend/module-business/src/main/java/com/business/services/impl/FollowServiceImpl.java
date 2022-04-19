package com.business.services.impl;

import com.business.services.AcountManagementService;
import com.business.services.FollowService;
import com.business.utilts.ApplicationUtils;
import com.core.entity.Follow;
import com.core.entity.Post;
import com.core.entity.User;
import com.core.model.AddFollow;
import com.core.model.user.UserDTO;
import com.core.repository.FollowRepository;
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
    private ModelMapper modelMapper;
    @Autowired
    private AcountManagementService acountManagementService;
    @Override
    public Page<Follow> getListFollow(int pageNumber, int size, String sortByProperties, String sortBy, String keyword) {
        Sort sort = ApplicationUtils.getSort(sortByProperties, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, size, sort);
        keyword = StringUtils.buildLikeExp(keyword);
        List<Follow> follows = new ArrayList<>();
        User currentUser = ApplicationUtils.getCurrentUser();
        Page<Follow> page = followRepository.findByCurrUser(
                currentUser.getUserId(),
                keyword,
                pageable
        );
        page.getContent().stream().forEach(u -> {
            follows.add(u);
        });
        return new PageImpl<>(follows, pageable, page.getTotalElements());
    }

    @Override
    public Follow addFollow(AddFollow addFollow) {
        Follow follow = new Follow();
        follow.setFollower(ApplicationUtils.getCurrentUser());
        follow.setFollowing(acountManagementService.findByUserIdAndIsDelete(addFollow.getFollowId(), Constants.DELETE.NORMAL));
        return followRepository.save(follow);
    }
}
