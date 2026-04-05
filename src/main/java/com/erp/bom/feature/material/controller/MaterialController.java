package com.erp.bom.feature.material.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.material.entity.Material;
import com.erp.bom.feature.material.service.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private static final Logger log = LoggerFactory.getLogger(MaterialController.class);

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Material>> getPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long materialTypeId) {
        log.info("[GET /api/materials/page] page={}, size={}, search={}, materialTypeId={}", page, size, search, materialTypeId);
        return ResponseEntity.ok(materialService.getPage(page, size, search, materialTypeId));
    }
}
