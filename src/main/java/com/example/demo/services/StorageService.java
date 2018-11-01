package com.example.demo.services;

import java.io.InputStream;

public interface StorageService {

    String upload(InputStream stream, String name);

    InputStream download(String file);
}
