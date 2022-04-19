package com.business.Controller;

import com.business.services.CommentService;
import com.core.config.ApplicationConfig;
import com.core.exception.BadRequestException;
import com.core.exception.PermissionException;
import com.core.model.Post.CreateCommentDTO;
import com.core.model.Post.CreatePostDTO;
import com.core.model.ResponseBody;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ApplicationConfig.MessageSourceVi messageSourceVi;
    //add comment
    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> createContract(
            @ApiParam(value = "JSon Object để tạo mới comment") @Valid @RequestBody CreateCommentDTO createCommentDTO
    ) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(commentService.addComment(createCommentDTO)).message(messageSourceVi.getMessageVi("OK002")).build());
    }

    //get Comment By postId
    @GetMapping("/get/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> getCommentByPostId(
            @ApiParam(value = "id của bài viết") @PathVariable("postId") Long postId
    ) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(commentService.getCommentByPostId(postId)).message(messageSourceVi.getMessageVi("OK002")).build());
    }
}

