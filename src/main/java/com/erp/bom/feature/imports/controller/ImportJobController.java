package com.erp.bom.feature.imports.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.imports.entity.ImportJob;
import com.erp.bom.feature.imports.service.ImportJobService;
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
@RequestMapping("/api/import-jobs")
public class ImportJobController {

    @Autowired
    private ImportJobService importJobService;

    @PostMapping
    public ResponseEntity<ImportJob> create(@RequestBody ImportJob importJob) {
        ImportJob created = importJobService.create(importJob);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportJob> getById(@PathVariable Long id) {
        ImportJob importJob = importJobService.getById(id);
        if (importJob == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(importJob);
    }

    @GetMapping
    public ResponseEntity<List<ImportJob>> getAll() {
        return ResponseEntity.ok(importJobService.getAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ImportJob>> getPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(importJobService.getPage(page, size, status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImportJob> update(@PathVariable Long id, @RequestBody ImportJob importJob) {
        ImportJob updated = importJobService.update(id, importJob);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        importJobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

