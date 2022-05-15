package com.core.repository.Post;

import com.core.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Page<Post> findByCreatedBy(Long userId, Pageable pageable);

    List<Post> findByCreatedBy(Long userId);

    long countByCreatedBy(Long userId);

    @Query("SELECT p from Post p where p.createdBy in :followingUsersIds")
    List<Post> findAllPostsByFollowedUsers(@Param("followingUsersIds") List<Long> followingUsersIds);

    @Query("select p from Post p where p.createdBy = :id")
    List<Post> findAllPostByCreate(Long id);

    @Query("select p from Post p where (p.isDelete = :isDelete) " +
            "AND (p.createdBy in :followingListUName) " +
            "AND (:keyword is null or p.title like :keyword or p.content like :keyword)"
    )
    Page<Post> searchAll(Long isDelete, String keyword, List<String> followingListUName, Pageable pageable);
}

