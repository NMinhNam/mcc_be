package com.erp.bom.feature.material.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.material.entity.MaterialType;
import com.erp.bom.feature.material.service.MaterialTypeService;
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
@RequestMapping("/api/material-types")
public class MaterialTypeController {

    @Autowired
    private MaterialTypeService materialTypeService;

    @PostMapping
    public ResponseEntity<MaterialType> create(@RequestBody MaterialType materialType) {
        MaterialType created = materialTypeService.create(materialType);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialType> getById(@PathVariable Long id) {
        MaterialType materialType = materialTypeService.getById(id);
        if (materialType == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(materialType);
    }

    @GetMapping
    public ResponseEntity<List<MaterialType>> getAll() {
        return ResponseEntity.ok(materialTypeService.getAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<MaterialType>> getPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(materialTypeService.getPage(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialType> update(@PathVariable Long id, @RequestBody MaterialType materialType) {
        MaterialType updated = materialTypeService.update(id, materialType);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

