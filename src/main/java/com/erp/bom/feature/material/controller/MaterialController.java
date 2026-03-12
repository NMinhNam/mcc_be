package com.erp.bom.feature.material.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.material.entity.Material;
import com.erp.bom.feature.material.service.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private static final Logger log = LoggerFactory.getLogger(MaterialController.class);

    @Autowired
    private MaterialService materialService;

    @PostMapping
    public ResponseEntity<Material> create(@RequestBody Material material) {
        log.info("[POST /api/materials] Payload: {}", material);
        Material created = materialService.create(material);
        log.info("[POST /api/materials] Response: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getById(@PathVariable Long id) {
        log.info("[GET /api/materials/{}]", id);
        Material material = materialService.getById(id);
        if (material == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(material);
    }

    @GetMapping
    public ResponseEntity<List<Material>> getAll() {
        log.info("[GET /api/materials]");
        return ResponseEntity.ok(materialService.getAll());
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

    @PutMapping("/{id}")
    public ResponseEntity<Material> update(@PathVariable Long id, @RequestBody Material material) {
        log.info("[PUT /api/materials/{}] Payload: {}", id, material);
        Material updated = materialService.update(id, material);
        log.info("[PUT /api/materials/{}] Response: {}", id, updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("[DELETE /api/materials/{}]", id);
        materialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

