package com.fileService.service;


import com.core.entity.AttachDocument;
import com.core.entity.User;
import com.core.model.Actor;
import com.core.model.file.DownloadOption;
import com.core.model.file.GetNumberPagesOfPdfResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;

public interface AttachDocumentService {

    void getPdfFile(
            AttachDocument attachDocument, DownloadOption downloadOption, IProcessFile processFile);

    AttachDocument populateOriginalAttachDocument(
            File finalOriginalFile, String fileId, Boolean isEncrypted, String contentType);

    GetNumberPagesOfPdfResponse getNumberPagesOfPdf(Long id);

    AttachDocument findById(Long id);

    AttachDocument save(AttachDocument attachDocument,Actor actor);

    AttachDocument populateOriginalAttachDocument(File finalOriginalFile);

    void downloadFile(HttpServletResponse response, Long id, DownloadOption downloadOption) throws Exception;

    interface IProcessFile {
        void do_(InputStream inputStream);
    }
}
