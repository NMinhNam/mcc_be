package com.erp.bom.feature.production.controller;

import com.erp.bom.feature.production.entity.ProductionOrderHardware;
import com.erp.bom.feature.production.service.ProductionOrderHardwareService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/production-order-hardware")
public class ProductionOrderHardwareController {

    private final ProductionOrderHardwareService productionOrderHardwareService;

    public ProductionOrderHardwareController(ProductionOrderHardwareService productionOrderHardwareService) {
        this.productionOrderHardwareService = productionOrderHardwareService;
    }

    @PostMapping
    public ResponseEntity<ProductionOrderHardware> create(@RequestBody ProductionOrderHardware hardware) {
        productionOrderHardwareService.save(hardware);
        return ResponseEntity.status(HttpStatus.CREATED).body(hardware);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionOrderHardware> getById(@PathVariable Long id) {
        ProductionOrderHardware hardware = productionOrderHardwareService.getById(id);
        if (hardware == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hardware);
    }

    @GetMapping
    public ResponseEntity<List<ProductionOrderHardware>> getAll() {
        return ResponseEntity.ok(productionOrderHardwareService.list());
    }

    @GetMapping("/order/{productionOrderId}")
    public ResponseEntity<List<ProductionOrderHardware>> getByProductionOrderId(@PathVariable Long productionOrderId) {
        return ResponseEntity.ok(productionOrderHardwareService.getByProductionOrderId(productionOrderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionOrderHardware> update(@PathVariable Long id, @RequestBody ProductionOrderHardware hardware) {
        hardware.setId(id);
        productionOrderHardwareService.updateById(hardware);
        return ResponseEntity.ok(productionOrderHardwareService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productionOrderHardwareService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
