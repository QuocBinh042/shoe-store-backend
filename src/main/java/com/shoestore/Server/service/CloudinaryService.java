package com.shoestore.Server.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    Map upload(MultipartFile file, String folder) throws IOException;
    Map listImagesInFolder(String folderPath) throws IOException;

}
