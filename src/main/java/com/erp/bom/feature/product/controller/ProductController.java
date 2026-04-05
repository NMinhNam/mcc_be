package com.erp.bom.feature.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.product.entity.Product;
import com.erp.bom.feature.product.service.BomRowService;
import com.erp.bom.feature.product.service.ProductService;
import com.erp.bom.feature.product.dto.BomRowRequest;
import com.erp.bom.feature.product.dto.BomRowResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final BomRowService bomRowService;

    public ProductController(ProductService productService, BomRowService bomRowService) {
        this.productService = productService;
        this.bomRowService = bomRowService;
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("[DELETE /products/{}]", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public void exportAll(HttpServletResponse response) throws IOException {
        log.info("[GET /products/export]");
        productService.exportAll(response);
    }

    @GetMapping("/{id}/bom")
    public ResponseEntity<List<BomRowResponse>> getBomRows(@PathVariable UUID id) {
        log.info("[GET /products/{}/bom]", id);
        return ResponseEntity.ok(bomRowService.getBomRowsByProductId(id));
    }

    @PostMapping("/{id}/bom")
    public ResponseEntity<Void> syncBomRows(@PathVariable UUID id, @RequestBody List<BomRowRequest> rows) {
        log.info("[POST /products/{}/bom] Rows: {}", id, rows.size());
        bomRowService.syncBomRows(id, rows);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/bom/import")
    public ResponseEntity<Void> importBom(@PathVariable UUID id, @RequestParam MultipartFile file) throws IOException {
        log.info("[POST /products/{}/bom/import]", id);
        bomRowService.importBom(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bom/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        log.info("[GET /products/bom/template]");
        productService.downloadBomTemplate(response);
    }

    @GetMapping("/{id}/bom/export")
    public void exportBom(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        log.info("[GET /products/{}/bom/export]", id);
        productService.exportWithBom(id, response);
    }
}
