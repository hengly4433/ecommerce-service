package com.bititech.ecommerce_service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
  String uploadProfileImage(String userEmail, MultipartFile file);
  String uploadProductMainImage(String sku, MultipartFile file);
  List<String> uploadProductGallery(String sku, List<MultipartFile> files);
}

