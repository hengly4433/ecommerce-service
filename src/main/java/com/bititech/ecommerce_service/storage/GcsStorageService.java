package com.bititech.ecommerce_service.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GcsStorageService implements StorageService {

  private final GcsStorageProperties props;

  private Storage storage() {
    try {
      if (props.getCredentialsJson() != null && !props.getCredentialsJson().isBlank()) {
        try (var is = new FileInputStream(props.getCredentialsJson())) {
          return StorageOptions.newBuilder()
              .setCredentials(GoogleCredentials.fromStream(is))
              .build().getService();
        }
      }
      return StorageOptions.getDefaultInstance().getService();
    } catch (IOException e) {
      throw new RuntimeException("GCS credentials error", e);
    }
  }

  private String put(String path, String contentType, byte[] bytes) {
    var bucket = storage().get(props.getBucket());
    if (bucket == null) throw new IllegalStateException("Bucket not found: " + props.getBucket());
    BlobId id = BlobId.of(props.getBucket(), path);
    BlobInfo info = BlobInfo.newBuilder(id).setContentType(contentType).build();
    bucket.create(info.getName(), bytes);
    return props.getPublicUrlPrefix() + "/" + props.getBucket() + "/" + path;
  }

  @Override
  public String uploadProfileImage(String userEmail, MultipartFile file) {
    var key = "profiles/" + userEmail + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
    try {
      return put(key, file.getContentType(), file.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String uploadProductMainImage(String sku, MultipartFile file) {
    var key = "products/" + sku + "/main-" + UUID.randomUUID() + "-" + file.getOriginalFilename();
    try {
      return put(key, file.getContentType(), file.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<String> uploadProductGallery(String sku, List<MultipartFile> files) {
    List<String> urls = new ArrayList<>();
    for (var f : files) {
      var key = "products/" + sku + "/gallery/" + UUID.randomUUID() + "-" + f.getOriginalFilename();
      try {
        urls.add(put(key, f.getContentType(), f.getBytes()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return urls;
  }
}

