package com.business.Controller;


import com.business.services.PostService;
import com.core.config.ApplicationConfig;
import com.core.entity.Post;
import com.core.exception.BadRequestException;
import com.core.exception.PermissionException;
import com.core.model.Post.CreatePostDTO;
import com.core.model.ResponseBody;
import com.core.model.account.UserSearchDto;
import com.core.utils.StringUtils;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/post")
public class PostController {


    @Autowired
    private PostService postService;

    @Autowired
    private ApplicationConfig.MessageSourceVi messageSourceVi;

    @PostMapping("/create")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> createContract(
            @ApiParam(value = "JSon Object để tạo mới hợp đồng") @Valid @RequestBody CreatePostDTO post
            ) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(postService.createPost(post)).message(messageSourceVi.getMessageVi("OK002")).build());
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> updateContract(
            @ApiParam(value = "JSon Object để cập nhật hợp đồng") @Valid @RequestBody CreatePostDTO post, @PathVariable("id") Long id
            ) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(postService.updatePost(post, id)).message(messageSourceVi.getMessageVi("OK002")).build());
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> getContract(@PathVariable("id") Long id) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(postService.getPost(id)).message(messageSourceVi.getMessageVi("OK002")).build());
    }

    @PostMapping("/search")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> getAllContract(
            @ApiParam(value = "Số trang", defaultValue = "0") @RequestParam(name = "page",
                    defaultValue = "0") int page,
            @ApiParam(value = "Số bản ghi / trang", defaultValue = "10") @RequestParam(name = "size",
                    defaultValue = "10") int size,
            @ApiParam(value = "Sắp xếp theo các thuộc tính", defaultValue = "modifiedDate") @RequestParam(
                    name = "properties", defaultValue = "modifiedDate", required = true) String sortByProperties,
            @ApiParam(value = "Loại sắp xếp ", defaultValue = "ASC") @RequestParam(name = "sortBy",
                    defaultValue = "ASC") String sortBy,
            @ApiParam(value = "Từ khóa tìm kiếm: có thể tìm theo userName, fullName,email,phoneNumber.") @RequestParam(name = "keyword",
                    required = false) String keyword) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(postService.getAllPost(page, size, sortByProperties, sortBy, keyword)).message(messageSourceVi.getMessageVi("OK002")).build());
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> deleteContract(@PathVariable("id") Long id) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(postService.deletePost(id)).message(messageSourceVi.getMessageVi("OK002")).build());
    }

}
