package com.erp.bom.feature.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.product.entity.Product;
import com.erp.bom.feature.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Product> createMultipart(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String imageBase64,
            @RequestParam(required = false) MultipartFile imageFile) {
        
        log.info("[POST /products multipart] Code: {}, Name: {}, HasBase64: {}, HasFile: {}", 
                code, name, imageBase64 != null, imageFile != null);
        
        Product created = productService.createProduct(code, name, note, imageBase64, imageFile);
        log.info("[POST /products multipart] Response: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Product> createJson(@RequestBody Product product) {
        log.info("[POST /products json] Payload: {}", product);
        
        // Handle Base64 image if provided
        String imageBase64 = product.getImage();
        Product created = productService.createProduct(
                product.getCode(), 
                product.getName(), 
                product.getNote(), 
                imageBase64, 
                null
        );
        log.info("[POST /products json] Response: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable UUID id) {
        log.info("[GET /products/{}]", id);
        Product product = productService.getById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        log.info("[GET /products]");
        var productList = productService.getAll();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Product>> getPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        log.info("[GET /products/page] page={}, size={}, search={}", page, size, search);
        return ResponseEntity.ok(productService.getPage(page, size, search));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable UUID id, @RequestBody Product product) {
        log.info("[PUT /products/{}] Payload: {}", id, product);
        Product updated = productService.update(id, product);
        log.info("[PUT /products/{}] Response: {}", id, updated);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> patchMultipart(
            @PathVariable UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String imageBase64,
            @RequestParam(required = false) MultipartFile imageFile) {
        log.info("[PATCH /products/{} multipart] Name: {}, HasBase64: {}, HasFile: {}", id, name, imageBase64 != null, imageFile != null);
        Product updated = productService.patchProduct(id, name, note, imageBase64, imageFile);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("[PATCH /products/{} multipart] Response: {}", id, updated);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping(value = "/{id}", consumes = {"application/json"})
    public ResponseEntity<Product> patchJson(@PathVariable UUID id, @RequestBody Product product) {
        log.info("[PATCH /products/{} json] Payload: {}", id, product);
        Product updated = productService.patchProduct(id, product.getName(), product.getNote(), product.getImage(), null);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("[PATCH /products/{} json] Response: {}", id, updated);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<Product> updateImage(
            @PathVariable UUID id,
            @RequestParam(required = false) MultipartFile image) {
        log.info("[PATCH /products/{}/image] Updating image", id);
        Product updated = productService.updateWithImage(id, null, null, image);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("[PATCH /products/{}/image] Response: {}", id, updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("[DELETE /products/{}]", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
