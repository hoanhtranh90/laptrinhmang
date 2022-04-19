/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.business.utilts;

import com.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sangnk
 */
@Slf4j
public class FileUtils {

//    public static final String UPLOAD_DIR = System.getProperty("user.home") + "/FileUploadAdfilex";
    public static final String UPLOAD_DIR = "upload/FileUploadAdfilex";

    public static List<String> saveUploadedFiles(MultipartFile[] files)
            throws IOException {
        // Make sure directory not exists!
//                File uploadDir = new File(org.apache.commons.lang3.StringUtils.isEmpty(folderDis) ? UPLOAD_DIR : folderDis);
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        List<String> listPath = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            String uploadFilePath
                    = new StringBuilder(UPLOAD_DIR + StringUtils.SLASH_RIGHT + file.getOriginalFilename()).toString();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFilePath);
            Files.write(path, bytes);
            log.info("UPLOAD FILE :" + uploadFilePath);
            listPath.add(uploadFilePath);
        }
        return listPath;
    }

    public static void writeFile(InputStream inputStream, OutputStream os) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        } finally {
            close(inputStream);
            close(os);
        }
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception ex) {
        }
    }
}
