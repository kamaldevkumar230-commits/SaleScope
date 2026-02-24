package com.salescope.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

@Service
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) {
    	
    	    try {
    	        Map<?, ?> uploadResult = cloudinary.uploader()
    	                .upload(file.getBytes(), Map.of());

    	        return uploadResult.get("secure_url").toString();

    	    } catch (Exception e) {
    	        e.printStackTrace();   // 🔥 IMPORTANT
    	        throw new RuntimeException("Image upload failed: " + e.getMessage());
    	    }
    	}
    
}



