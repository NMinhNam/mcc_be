package com.erp.bom.feature.product.controller;

import com.erp.bom.feature.product.dto.BomRowRequest;
import com.erp.bom.feature.product.dto.BomRowResponse;
import com.erp.bom.feature.product.dto.BomSyncRequest;
import com.erp.bom.feature.product.service.BomRowService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/products/{productId}/bom")
public class BomController {

    private final BomRowService bomRowService;

    public BomController(BomRowService bomRowService) {
        this.bomRowService = bomRowService;
    }

    @GetMapping
    public ResponseEntity<List<BomRowResponse>> getBomRows(@PathVariable UUID productId) {
        List<BomRowResponse> rows = bomRowService.getBomRowsByProductId(productId);
        return ResponseEntity.ok(rows);
    }

    @PutMapping
    public ResponseEntity<Void> syncBom(
            @PathVariable UUID productId,
            @Valid @RequestBody BomSyncRequest request) {
        bomRowService.syncBomRows(productId, request.getRows());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> addBomRow(
            @PathVariable UUID productId,
            @Valid @RequestBody List<BomRowRequest> requests) {
        bomRowService.syncBomRows(productId, requests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> addBomRows(
            @PathVariable UUID productId,
            @Valid @RequestBody List<BomRowRequest> requests) {
        bomRowService.syncBomRows(productId, requests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importBom(@PathVariable UUID productId, @RequestParam("file") MultipartFile file) throws IOException {
        bomRowService.importBom(productId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/export")
    public void exportBom(@PathVariable UUID productId, HttpServletResponse response) throws IOException {
        bomRowService.exportBom(productId, response);
    }
}
