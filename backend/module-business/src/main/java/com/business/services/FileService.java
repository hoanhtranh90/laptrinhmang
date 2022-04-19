/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.business.services;

import com.core.entity.AttachDocument;
import com.core.entity.VbAttachment;
import com.core.model.Actor;
import com.core.model.file.DownloadOption;
import com.core.model.file.ExtractFileResult;
import com.core.model.file.GetNumberPagesOfPdfResponse;
import com.core.model.file.UploadOption;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 *
 * @author sadfsafbhsaid
 */
public interface FileService {

    AttachDocument uploadFile(MultipartFile multipartFile, UploadOption uploadOption, Actor actor);
    
    InputStream getInputStream(Long fileServiceId, DownloadOption downloadOption);
    
    ExtractFileResult convertPdfToImage(VbAttachment vbAttachment, Actor actor);
    
    GetNumberPagesOfPdfResponse getNumberPagesOfPdf(Long fileServiceId);

}
