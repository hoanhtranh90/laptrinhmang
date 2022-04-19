package com.business.services.impl;

import com.business.services.PostService;
import com.business.utilts.ApplicationUtils;
import com.core.entity.Post;
import com.core.entity.User;
import com.core.exception.BadRequestException;
import com.core.model.Post.CreatePostDTO;
import com.core.model.account.UserBasicDto;
import com.core.repository.Post.PostRepository;
import com.core.utils.Constants;
import com.core.utils.H;
import com.core.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceIml implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Post createPost(CreatePostDTO post) {
        Post post1 = new Post();
        post1.setTitle(post.getTitle());
        post1.setContent(post.getContent());
        return postRepository.save(post1);
    }

    @Override
    public Post updatePost(CreatePostDTO post, Long id) throws BadRequestException {
        Post post1 = postRepository.findById(id).get();
        if(!H.isTrue(post1)) throw new BadRequestException("Post not found");
        if(H.isTrue(post.getTitle())) post1.setTitle(post.getTitle());
        if(H.isTrue(post.getContent())) post1.setContent(post.getContent());
        return postRepository.save(post1);
    }

    @Override
    public Post getPost(Long id) {
        return postRepository.findById(id).get();
    }

    @Override
    public Page<Post> getAllPost(int pageNumber, int size, String sortByProperties, String sortBy, String keyword) {
        Sort sort = ApplicationUtils.getSort(sortByProperties, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, size, sort);
        keyword = StringUtils.buildLikeExp(keyword);
        List<Post> posts = new ArrayList<>();
        Page<Post> page = postRepository.searchAll(
                Constants.DELETE.NORMAL,
                keyword,
                pageable
        );
        page.getContent().stream().forEach(u -> {
            posts.add((u));
        });
        return new PageImpl<>(posts, pageable, page.getTotalElements());
    }

    @Override
    public Post deletePost(Long id) {
        Post post = postRepository.findById(id).get();
        post.setIsDelete(Constants.DELETE.DELETED);
        return postRepository.save(post);
    }
}
