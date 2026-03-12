package com.erp.bom.feature.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.cloudinary.CloudinaryService;
import com.erp.bom.feature.product.entity.Product;
import com.erp.bom.feature.product.mapper.ProductMapper;
import com.erp.bom.feature.product.service.BomRowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private BomRowService bomRowService;

    public Product create(Product product) {
        // Handle image upload to Cloudinary if base64 image is provided
        if (StringUtils.hasText(product.getImage()) && product.getImage().startsWith("data:image")) {
            try {
                String imageUrl = cloudinaryService.uploadFromBase64(product.getImage());
                product.setImage(imageUrl);
            } catch (IOException e) {
                log.error("Failed to upload image to Cloudinary: {}", e.getMessage());
            }
        }
        productMapper.insert(product);
        return product;
    }

    public Product createProduct(String code, String name, String note, String imageBase64, MultipartFile imageFile) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setNote(note);

        // Handle image: prefer Base64 if provided, otherwise use file
        if (StringUtils.hasText(imageBase64) && imageBase64.startsWith("data:image")) {
            try {
                String imageUrl = cloudinaryService.uploadFromBase64(imageBase64);
                product.setImage(imageUrl);
            } catch (IOException e) {
                log.error("Failed to upload base64 image to Cloudinary: {}", e.getMessage());
            }
        } else if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.upload(imageFile);
                product.setImage(imageUrl);
            } catch (IOException e) {
                log.error("Failed to upload file image to Cloudinary: {}", e.getMessage());
            }
        }

        productMapper.insert(product);
        return product;
    }

    public Product createWithImage(String code, String name, String note, MultipartFile imageFile) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setNote(note);

        // Upload image if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.upload(imageFile);
                product.setImage(imageUrl);
            } catch (IOException e) {
                log.error("Failed to upload image to Cloudinary: {}", e.getMessage());
            }
        }

        productMapper.insert(product);
        return product;
    }

    public Product updateWithImage(UUID id, String name, String note, MultipartFile newImageFile) {
        Product existing = productMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        // Update basic fields
        if (StringUtils.hasText(name)) {
            existing.setName(name);
        }
        if (StringUtils.hasText(note)) {
            existing.setNote(note);
        }

        // Handle image update
        if (newImageFile != null && !newImageFile.isEmpty()) {
            try {
                // Check if product already has an image - if yes, delete old one to save storage
                if (StringUtils.hasText(existing.getImage())) {
                    cloudinaryService.delete(existing.getImage());
                    log.info("Deleted old image from Cloudinary: {}", existing.getImage());
                }

                // Upload new image
                String newImageUrl = cloudinaryService.upload(newImageFile);
                existing.setImage(newImageUrl);
            } catch (IOException e) {
                log.error("Failed to upload new image to Cloudinary: {}", e.getMessage());
            }
        }

        productMapper.updateById(existing);
        return existing;
    }

    public Product getById(UUID id) {
        return productMapper.selectById(id);
    }

    public List<Product> getAll() {
        log.info("[GET /products]");
        var selectListProductResult = productMapper.selectList(null);
        if (selectListProductResult.isEmpty()) {
            log.info("Product list is empty");
        }
        log.info("List Product result: {}", selectListProductResult);
        return selectListProductResult;
    }

    public Page<Product> getPage(int page, int size, String search) {
        Page<Product> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(search)) {
            wrapper.like(Product::getName, search)
                    .or()
                    .like(Product::getCode, search);
        }
        wrapper.orderByDesc(Product::getId);
        return productMapper.selectPage(pageParam, wrapper);
    }

    public Product update(UUID id, Product product) {
        Product existing = productMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        product.setId(id);
        productMapper.updateById(product);
        return productMapper.selectById(id);
    }

    public Product patch(UUID id, Product product) {
        Product existing = productMapper.selectById(id);
        if (existing == null) {
            return null;
        }
        if (StringUtils.hasText(product.getName())) {
            existing.setName(product.getName());
        }
        if (StringUtils.hasText(product.getImage())) {
            // If new image is base64, upload to Cloudinary
            if (product.getImage().startsWith("data:image")) {
                try {
                    // Check if product already has an image - delete old one to save storage
                    if (StringUtils.hasText(existing.getImage())) {
                        cloudinaryService.delete(existing.getImage());
                        log.info("Deleted old image from Cloudinary: {}", existing.getImage());
                    }
                    String imageUrl = cloudinaryService.uploadFromBase64(product.getImage());
                    existing.setImage(imageUrl);
                } catch (IOException e) {
                    log.error("Failed to upload image to Cloudinary: {}", e.getMessage());
                }
            } else {
                // If not base64, assume it's already a URL
                existing.setImage(product.getImage());
            }
        }
        productMapper.updateById(existing);
        return existing;
    }

    public Product patchProduct(UUID id, String name, String note, String imageBase64, MultipartFile imageFile) {
        Product existing = productMapper.selectById(id);
        if (existing == null) {
            return null;
        }

        // Update basic fields
        if (StringUtils.hasText(name)) {
            existing.setName(name);
        }
        if (StringUtils.hasText(note)) {
            existing.setNote(note);
        }

        // Handle image: prefer Base64 if provided, otherwise use file
        if (StringUtils.hasText(imageBase64) && imageBase64.startsWith("data:image")) {
            try {
                // Delete old image if exists
                if (StringUtils.hasText(existing.getImage())) {
                    cloudinaryService.delete(existing.getImage());
                    log.info("Deleted old image from Cloudinary: {}", existing.getImage());
                }
                String imageUrl = cloudinaryService.uploadFromBase64(imageBase64);
                existing.setImage(imageUrl);
            } catch (IOException e) {
                log.error("Failed to upload base64 image to Cloudinary: {}", e.getMessage());
            }
        } else if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Delete old image if exists
                if (StringUtils.hasText(existing.getImage())) {
                    cloudinaryService.delete(existing.getImage());
                    log.info("Deleted old image from Cloudinary: {}", existing.getImage());
                }
                String imageUrl = cloudinaryService.upload(imageFile);
                existing.setImage(imageUrl);
            } catch (IOException e) {
                log.error("Failed to upload file image to Cloudinary: {}", e.getMessage());
            }
        }

        productMapper.updateById(existing);
        return existing;
    }

    public void delete(UUID id) {
        // Get product to check for image
        Product product = productMapper.selectById(id);
        if (product == null) {
            return;
        }

        // Delete image from Cloudinary if exists
        if (StringUtils.hasText(product.getImage())) {
            try {
                cloudinaryService.delete(product.getImage());
                log.info("Deleted product image from Cloudinary: {}", product.getImage());
            } catch (IOException e) {
                log.error("Failed to delete product image from Cloudinary: {}", e.getMessage());
            }
        }

        // Delete BOM rows first (explicit delete, though DB cascade should handle it)
        bomRowService.deleteByProductId(id);

        // Delete product
        productMapper.deleteById(id);
        log.info("Deleted product with id: {}", id);
    }
}
