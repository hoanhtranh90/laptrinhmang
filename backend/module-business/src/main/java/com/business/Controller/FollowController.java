package com.business.Controller;

import com.business.services.FollowService;
import com.core.config.ApplicationConfig;
import com.core.exception.BadRequestException;
import com.core.exception.PermissionException;
import com.core.model.AddFollow;
import com.core.model.ResponseBody;
import com.core.model.search.SearchFollowDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private ApplicationConfig.MessageSourceVi messageSourceVi;
    //get list follow
   /* @PostMapping("/list-follow")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> getAllFollow(
            @ApiParam(value = "Số trang", defaultValue = "0") @RequestParam(name = "page",
                    defaultValue = "0") int page,
            @ApiParam(value = "Số bản ghi / trang", defaultValue = "10") @RequestParam(name = "size",
                    defaultValue = "10") int size,
            @ApiParam(value = "Sắp xếp theo các thuộc tính", defaultValue = "modifiedDate") @RequestParam(
                    name = "properties", defaultValue = "modifiedDate", required = true) String sortByProperties,
            @ApiParam(value = "Loại sắp xếp ", defaultValue = "ASC") @RequestParam(name = "sortBy",
                    defaultValue = "ASC") String sortBy,
            @ApiParam(value = "Từ khóa tìm kiếm: có thể tìm theo userName, fullName,email,phoneNumber.") @RequestParam(name = "keyword",
                    required = false) String keyword,
            @RequestBody SearchFollowDTO searchFollowDTO
            ) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(followService.getListFollow(page, size, sortByProperties, sortBy, keyword, searchFollowDTO)).message(messageSourceVi.getMessageVi("OK002")).build());
    }*/

    //add follow
    @PostMapping("/follow")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> addFollow(
            @Valid @RequestBody AddFollow addFollow) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(followService.addFollow(addFollow)).message(messageSourceVi.getMessageVi("OK002")).build());
    }
}

