package com.fileService.service;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DownloadService {
    void downloadFile(HttpServletResponse response, String fileName, String mediaType,
                      DownloadServiceImpl.IDownloadProcessor processor) throws IOException;
}
