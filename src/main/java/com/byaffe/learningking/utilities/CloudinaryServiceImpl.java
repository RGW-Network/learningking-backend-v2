package com.byaffe.learningking.utilities;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CloudinaryServiceImpl implements ImageStorageService {
    private static String CLOUDINARY_CLOUD_NAME = "revival-gateway";
    private static String CLOUDINARY_API_KEY = "114516888855596";
    private static String CLOUDINARY_API_SECRET = "gG2NUayJtKGuFcQln1yvxYPTMzQ";
    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", CLOUDINARY_CLOUD_NAME,
            "api_key", CLOUDINARY_API_KEY,
            "api_secret", CLOUDINARY_API_SECRET,
            "secure", true));


    public String uploadImage(byte[] contents, String public_id) {
        System.out.println("Started image upload...");
        try {
            Map uploadResult = cloudinary.uploader().upload(contents, ObjectUtils.asMap("public_id", public_id));
            String imageUrl = uploadResult.get("secure_url").toString();
            System.out.println("Image url = " + imageUrl);
            return imageUrl;
        } catch (IOException ex) {
            Logger.getLogger(CloudinaryServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public String uploadImage(MultipartFile contents, String public_id) {
        try {
            return uploadImage(contents.getBytes(), public_id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
