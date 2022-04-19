package com.fileService.controller;

import com.core.config.ApplicationConfig;
import com.core.model.ResponseBody;
import com.core.model.file.DownloadOption;
import com.core.model.file.UploadFileDTO;
import com.fileService.service.AttachDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private AttachDocumentService attachDocumentService;

    @Autowired
    private ApplicationConfig.MessageSourceVi messageSourceVi;
    @GetMapping("/downloadFile/{id}")
    public void downloadFile(
            HttpServletResponse response, @PathVariable Long id, DownloadOption downloadOption) throws Exception {
        attachDocumentService.downloadFile(response, id, downloadOption);
    }


}
