package com.fileService.service;


import com.core.entity.AttachDocument;
import java.io.*;
import java.util.List;

public interface StorageService {

    File mergePdfFiles(File desFile, InputStream... inputStreams);

    String getSubFolder();

    File getLocalFile(String id, String subFolder);

    File getLocalTempFile();

    void writeFile(InputStream inputStream, OutputStream os) throws IOException;

    File saveFile(String id, String subFolder, InputStream inputStream, Boolean isEncrypt);

    File saveFile(String id, String subFolder, Boolean isEncrypt, ISaveFileLogic iSaveFileLogic);

    String getFont(String path);

    void addWaterMark(InputStream inputStream, String waterMarkText, OutputStream outputStream);

    void deleteFile(File file);

    void deleteFile(List<File> files);

    InputStream getInputStream(InputStream inputStream, Boolean isEncrypt);

    InputStream getInputStream(AttachDocument attachDocument) throws FileNotFoundException;

    void getFile(String filePath, Boolean isEncrypt, IGetFileLogic2 iGetFileLogic);
}
