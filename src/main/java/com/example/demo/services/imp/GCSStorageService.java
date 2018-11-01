package com.example.demo.services.imp;

import com.example.demo.services.StorageService;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GCSStorageService implements StorageService {

    @Autowired
    private Storage storage;

    @Value("${application.uploadService.bucketName}")
    private String bucketName;

    @Override
    public String upload(InputStream stream, String fileName) {
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fileName).setAcl(new ArrayList<>(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))).build(), stream);

        // return the public download link
        blobInfo.toBuilder().build().getBlobId();
        return blobInfo.getMediaLink();
    }

    @Override
    public InputStream download(String fileName) {
        BlobId blobId = BlobId.of(bucketName, fileName);
        ReadChannel reader = storage.get(blobId).reader();
        return Channels.newInputStream(reader);
    }
}

