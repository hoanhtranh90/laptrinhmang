package com.business.services;


import com.core.entity.Post;
import com.core.exception.BadRequestException;
import com.core.model.Post.CreatePostDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    Post createPost(CreatePostDTO post);

    Post updatePost(CreatePostDTO post, Long id) throws BadRequestException;

    Post getPost(Long id);


    Page<Post> getAllPost(int page, int size, String sortByProperties, String sortBy, String keyword);

    Post deletePost(Long id);
}
