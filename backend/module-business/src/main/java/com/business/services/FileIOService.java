/**
 * Welcome developer friend. PC ospAdfilex-smartTeleSale-service FileIOService.java 5:06:27 PM
 */
package com.business.services;

import com.business.services.impl.FileIOServiceImpl;
import com.core.entity.VbAttachment;
import com.core.exception.BadRequestException;
import com.core.model.Actor;
import com.core.model.file.DownloadOption;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 *
 * @author Nguyen_Toan
 */
public interface FileIOService {

    VbAttachment uploadFile(MultipartFile fileUpoad, Long objectType,  String storageType, String note, Actor actor);

    VbAttachment findByAttachmentId(Long attachmentId, Long objectType);

    List<VbAttachment> findByAttachmentIds(List<Long> attachmentIds, Long objectType);

    ResponseEntity<InputStreamResource> downloadFile(Long attachmentId, DownloadOption downloadOption);

    List<String> getListFile(String path);

    void savaAttachment(VbAttachment attachment);

    /**
     *
     * @param fileName
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws Exception
     */
    String getContentAsStringFile(String fileName) throws URISyntaxException, IOException, Exception;

    FileIOServiceImpl getFileIOServiceImpl();

    List<VbAttachment> findByObjectIdAndObjectType(Long objectId, Long objectType);

    List<VbAttachment> getFileByObjectIdAndObjectType(Long objectId, Long objectType);

    Boolean updateFileForObject(Long objectId, List<Long> listFileIds) throws BadRequestException;

    VbAttachment deleteFile(Long attachmentId);

    VbAttachment updateFileInfo(Long attachmentId, VbAttachment vbAttachment);

    String downloadFileBase64(Long attachmentId) throws IOException;
}
