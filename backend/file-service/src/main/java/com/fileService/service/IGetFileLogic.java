package com.fileService.service;

import java.io.InputStream;

public interface IGetFileLogic<T> {
    T do_(InputStream inputStream);
}
