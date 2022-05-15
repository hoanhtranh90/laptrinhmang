package com.core.repository;

import com.core.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("select f from Follow f where (f.follower.userId = :userId) " +
            "and (:keyword is null or f.follower.userName = :keyword)")
    Page<Follow> findByCurrUser(Long userId, String keyword, Pageable pageable);

    @Query("select f from Follow f where (f.follower.userId = :currentUser) " +
            "and (:user is null or f.following.userId = :user)")
    Follow findByFollowerAndFollowing(Long currentUser, Long user);
}
