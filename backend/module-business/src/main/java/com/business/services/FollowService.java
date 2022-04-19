package com.business.services;

import com.core.entity.Follow;
import com.core.model.AddFollow;
import com.core.model.user.UserDTO;
import org.springframework.data.domain.Page;

public interface FollowService {

    Page<Follow> getListFollow(int page, int size, String sortByProperties, String sortBy, String keyword);

    Follow addFollow(AddFollow addFollow);
}
