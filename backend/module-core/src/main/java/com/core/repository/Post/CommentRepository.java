package com.core.repository.Post;

import com.core.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT COUNT(v.id) from Comment v where v.post.id = :postId")
    long countByPostId(@Param("postId") Long postId);

    Page<Comment> findCommentsByPostId(@Param("postId") Long postId, Pageable pageable);

    List<Comment> findCommentsByPostId(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c where c.id = :commentId and c.post.id = :postId")
    Optional<Comment> findCommentByPostIdAndCommentId(@Param("postId") Long postId, @Param("commentId") Long commentId);

    @Query("SELECT c FROM Comment c where (c.isDelete =:isDelete) " +
            "and (c.post.id = :postId)")
    List<Comment> findByPostIdAndIsDelete(Long postId, Long isDelete);
}
