package com.erp.bom.feature.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);
    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.url}") String cloudinaryUrl) {
        this.cloudinary = new Cloudinary(cloudinaryUrl);
    }

    /**
     * Upload image from base64 string
     */
    public String uploadFromBase64(String base64Image) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        // Remove data:image/xxx;base64, prefix if exists
        String imageData = base64Image;
        if (base64Image.contains(",")) {
            imageData = base64Image.substring(base64Image.indexOf(",") + 1);
        }

        byte[] imageBytes = Base64.getDecoder().decode(imageData);
        String publicId = "products/" + UUID.randomUUID();

        Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                "public_id", publicId,
                "folder", "erp_bom",
                "overwrite", true
        ));

        return (String) uploadResult.get("secure_url");
    }

    /**
     * Upload image from MultipartFile
     */
    public String upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String publicId = "products/" + UUID.randomUUID();

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", publicId,
                "folder", "erp_bom",
                "overwrite", true
        ));

        return (String) uploadResult.get("secure_url");
    }

    /**
     * Delete image by URL
     */
    public void delete(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        // Extract public_id from URL
        // Format: https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{public_id}.{format}
        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            // Log but don't fail if delete fails
            log.error("Failed to delete image: {}", e.getMessage());
        }
    }

    private String extractPublicId(String imageUrl) {
        // Simple extraction - in production might need more robust parsing
        if (imageUrl.contains("/products/")) {
            int start = imageUrl.indexOf("/products/") + "/products/".length();
            int end = imageUrl.lastIndexOf(".");
            if (end > start) {
                return "products/" + imageUrl.substring(start, end);
            }
        }
        return null;
    }
}
