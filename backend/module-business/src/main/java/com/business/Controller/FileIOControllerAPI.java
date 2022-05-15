/**
 * Welcome developer friend. PC ospAdfilex-smartTeleSale-controller FileIOControllerAPI.java 5:08:36
 * PM
 */
package com.business.Controller;


import com.business.services.FileIOService;
import com.business.utilts.ApplicationUtils;
import com.core.config.ApplicationConfig;
import com.core.entity.VbAttachment;
import com.core.exception.BadRequestException;
import com.core.exception.PermissionException;
import com.core.model.ResponseBodyWithType;
import com.core.model.file.DownloadOption;
import com.core.model.file.UploadFileDTO;
import com.core.utils.StringUtils;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Nguyen_Toan
 */
@RestController
@RequestMapping("/files")
@Api
@Validated
public class FileIOControllerAPI {

    @Autowired
    private FileIOService fileIOService;

    @Autowired
    private ApplicationConfig.MessageSourceVi messageSourceVi;



    private static Logger LOG = LoggerFactory.getLogger(FileIOControllerAPI.class);

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(
            @RequestParam(name = "file") MultipartFile uploadFile,
            @RequestParam(name = "objectType") Long objectType,
            @RequestParam(name = "note", required = false) String note,
            @RequestParam(name = "storageType", required = false) String storageType
    ) {

        return ResponseEntity.ok(ResponseBodyWithType.builder().body(fileIOService.uploadFile(uploadFile, objectType, storageType, note, ApplicationUtils.getUserTokenInfo().getActor()))
                .message(messageSourceVi.getMessageVi("OK002")).build());
    }


    @RequestMapping(path = "/downloadFile/{attachmentId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp download file tu server ",
            value = "(File Management Page) API File Management Page ",
            authorizations = {
                @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Data Response Retrieved."),
        @ApiResponse(code = 500, message = "Internal Server Error."),
        @ApiResponse(code = 400, message = "Bad Request cause data input."),
        @ApiResponse(code = 404, message = "Not found."),
        @ApiResponse(code = 403, message = "Access Denied Or Any More."),
        @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long attachmentId, DownloadOption downloadOption) throws IOException {

        return fileIOService.downloadFile(attachmentId, downloadOption);

    }

    @RequestMapping(path = "/downloadFileBase64/{attachmentId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp download file tu server ",
            value = "(File Management Page) API File Management Page ",
            authorizations = {
                    @Authorization(value = StringUtils.API_KEY)})
    public ResponseEntity<?> downloadFileBase64(@PathVariable Long attachmentId) throws IOException {

//        return fileIOService.downloadFileBase64(attachmentId);
        return ResponseEntity.ok(ResponseBodyWithType.builder().body(fileIOService.downloadFileBase64(attachmentId)).message(messageSourceVi.getMessageVi("OK002")).build());

    }
    //delete file
    @DeleteMapping("/deleteFile/{attachmentId}")
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp xóa file  ",
            value = "(File Management Page) API File delete  ",
            authorizations = {
                    @Authorization(value = StringUtils.API_KEY)})
    public ResponseEntity<?> deleteFile(@PathVariable Long attachmentId) {
        return ResponseEntity.ok(ResponseBodyWithType.builder().body(fileIOService.deleteFile(attachmentId)).message(messageSourceVi.getMessageVi("OK002")).build());
    }

    @PutMapping("/updateFileInfo/{attachmentId}")
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp cập nhật các thông tin file  ",
            value = "(File Management Page) API cập nhật thông tin cho file  ",
            authorizations = {
                    @Authorization(value = StringUtils.API_KEY)})
    public ResponseEntity<?> updateFile(@PathVariable Long attachmentId, @RequestBody VbAttachment vbAttachment) {
        return ResponseEntity.ok(ResponseBodyWithType.builder().body(fileIOService.updateFileInfo(attachmentId, vbAttachment)).message(messageSourceVi.getMessageVi("OK002")).build());
    }


    @PutMapping("/upload")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data Response Retrieved."),
            @ApiResponse(code = 500, message = "Internal Server Error."),
            @ApiResponse(code = 400, message = "Bad Request cause data input."),
            @ApiResponse(code = 404, message = "Not found."),
            @ApiResponse(code = 403, message = "Access Denied Or Any More."),
            @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<?> updateImage(
            @ApiParam(value = "JSon Object để thêm file cho sản phẩm")
            @RequestBody UploadFileDTO uploadFileForobj)
            throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBodyWithType.builder().body(fileIOService.updateFileForObject(uploadFileForobj.getObjectId(), uploadFileForobj.getListFileIds())).message(messageSourceVi.getMessageVi("OK002")).build());
    }

    //get file by objectId and type
    @GetMapping("/getFileByObjectIdAndObjectType")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = "API sẽ được gọi trong trường hợp lấy file theo objectId và type",
            value = "(Account Management Page) API get file by objectId and type",
            authorizations = {
                    @Authorization(value = StringUtils.API_KEY)})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data Response Retrieved."),
            @ApiResponse(code = 500, message = "Internal Server Error."),
            @ApiResponse(code = 400, message = "Bad Request cause data input."),
            @ApiResponse(code = 404, message = "Not found."),
            @ApiResponse(code = 403, message = "Access Denied Or Any More."),
            @ApiResponse(code = 401, message = "Unauthorized.")})
    public ResponseEntity<?> getFileByObjectIdAndType(
            @ApiParam(value = "objectId")
            @RequestParam(name = "objectId") Long objectId,
            @ApiParam(value = "objectType")
            @RequestParam(name = "objectType") Long objectType)
            throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBodyWithType.builder().body(fileIOService.getFileByObjectIdAndObjectType(objectId, objectType)).message(messageSourceVi.getMessageVi("OK002")).build());
    }

    public static void main(String[] args) {
        String pathStr = "upload/FileUploadAdfilex/Screenshot from 2021-03-02 08-30-32.png";

        String fileName = pathStr.substring(pathStr.lastIndexOf("/") + 1);

        System.out.println(fileName);
    }
}
