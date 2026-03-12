package com.erp.bom.feature.hardware.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.hardware.entity.HardwareItem;
import com.erp.bom.feature.hardware.service.HardwareItemService;
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
@RequestMapping("/api/hardware-items")
public class HardwareItemController {

    @Autowired
    private HardwareItemService hardwareItemService;

    @PostMapping
    public ResponseEntity<HardwareItem> create(@RequestBody HardwareItem hardwareItem) {
        HardwareItem created = hardwareItemService.create(hardwareItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HardwareItem> getById(@PathVariable Long id) {
        HardwareItem hardwareItem = hardwareItemService.getById(id);
        if (hardwareItem == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hardwareItem);
    }

    @GetMapping
    public ResponseEntity<List<HardwareItem>> getAll() {
        return ResponseEntity.ok(hardwareItemService.getAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<HardwareItem>> getPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(hardwareItemService.getPage(page, size, search));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HardwareItem> update(@PathVariable Long id, @RequestBody HardwareItem hardwareItem) {
        HardwareItem updated = hardwareItemService.update(id, hardwareItem);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hardwareItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

