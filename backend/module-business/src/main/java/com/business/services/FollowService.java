package com.business.services;

import com.core.entity.Follow;
import com.core.model.AddFollow;
import com.core.model.FollowResponseDTO;
import com.core.model.search.SearchFollowDTO;
import com.core.model.user.UserDTO;
import org.springframework.data.domain.Page;

public interface FollowService {

    UserDTO addFollow(AddFollow addFollow);
}
