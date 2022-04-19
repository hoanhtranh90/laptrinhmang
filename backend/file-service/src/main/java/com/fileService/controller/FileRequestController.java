package com.fileService.controller;

import com.core.entity.AttachDocument;
import com.core.model.Actor;
import com.core.model.file.ExtractFileOption;
import com.core.model.file.ExtractFileResult;
import com.core.model.file.UploadOption;
import com.fileService.service.ConvertPdfToImageService;
import com.fileService.service.UploadFileService;
import com.fileService.service.UploadImageFileBase64Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Scope("request")
@RestController
@RequestMapping("/file")
public class FileRequestController {
    
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private UploadImageFileBase64Service uploadImageFileBase64;
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private ConvertPdfToImageService convertPdfToImageService;
    
    @PostMapping("/uploadFile")
    public AttachDocument uploadFile(
            @RequestParam(name = "file") MultipartFile uploadFile, UploadOption uploadOption) throws Exception {
         JsonNode parsedNodes = mapper.readValue(uploadOption.getActor().toString(), JsonNode.class);
        Actor actor = mapper.convertValue(parsedNodes, Actor.class);
        return uploadFileService.uploadFile(uploadOption, uploadFile, actor);
    }
    
    @PostMapping("/uploadImageFileBase64")
    public AttachDocument uploadImageFileBase64(UploadOption uploadOption) throws Exception {
        JsonNode parsedNodes = mapper.readValue(uploadOption.getActor().toString(), JsonNode.class);
        Actor actor = mapper.convertValue(parsedNodes, Actor.class);
        return uploadImageFileBase64.uploadImageFileBase64(uploadOption.getFileBase64(), uploadOption, actor);
    }
    
    @PostMapping("/convertPdfToImage")
    public ExtractFileResult convertPdfToImage(ExtractFileOption extractFileOption) throws FileNotFoundException {
        
        return convertPdfToImageService.convertPdfToImage(extractFileOption, extractFileOption.getActor());
    }
}
