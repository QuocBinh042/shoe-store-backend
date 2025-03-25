package com.shoestore.Server.service.impl;

import com.cloudinary.Cloudinary;
import com.shoestore.Server.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile file, String folder) throws IOException {
        File uploadedFile = convertMultiPartToFile(file);
        Map options = new HashMap<>();
        options.put("folder", folder);
        Map uploadResult = cloudinary.uploader().upload(uploadedFile, options);
        uploadedFile.delete();
        return uploadResult;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    @Override
    public Map listImagesInFolder(String folderPath) throws IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("type", "upload");
        options.put("prefix", folderPath);
        try {
            return cloudinary.api().resources(options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}