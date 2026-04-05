package com.erp.bom.feature.material.controller;

import com.erp.bom.feature.material.entity.MaterialType;
import com.erp.bom.feature.material.service.MaterialTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/material-types")
public class MaterialTypeController {

    private final MaterialTypeService materialTypeService;

    public MaterialTypeController(MaterialTypeService materialTypeService) {
        this.materialTypeService = materialTypeService;
    }

    @GetMapping
    public ResponseEntity<List<MaterialType>> getAll() {
        return ResponseEntity.ok(materialTypeService.getAll());
    }
}
