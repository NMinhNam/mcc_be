package com.erp.bom.feature.production.controller;

import com.erp.bom.feature.production.entity.ProductionOrderCost;
import com.erp.bom.feature.production.service.ProductionOrderCostService;
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
@RequestMapping("/api/production-order-costs")
public class ProductionOrderCostController {

    private final ProductionOrderCostService productionOrderCostService;

    public ProductionOrderCostController(ProductionOrderCostService productionOrderCostService) {
        this.productionOrderCostService = productionOrderCostService;
    }

    @PostMapping
    public ResponseEntity<ProductionOrderCost> create(@RequestBody ProductionOrderCost cost) {
        productionOrderCostService.save(cost);
        return ResponseEntity.status(HttpStatus.CREATED).body(cost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionOrderCost> getById(@PathVariable Long id) {
        ProductionOrderCost cost = productionOrderCostService.getById(id);
        if (cost == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cost);
    }

    @GetMapping
    public ResponseEntity<List<ProductionOrderCost>> getAll() {
        return ResponseEntity.ok(productionOrderCostService.list());
    }

    @GetMapping("/order/{productionOrderId}")
    public ResponseEntity<ProductionOrderCost> getByProductionOrderId(@PathVariable Long productionOrderId) {
        return ResponseEntity.ok(productionOrderCostService.getByProductionOrderId(productionOrderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionOrderCost> update(@PathVariable Long id, @RequestBody ProductionOrderCost cost) {
        cost.setId(id);
        productionOrderCostService.updateById(cost);
        return ResponseEntity.ok(productionOrderCostService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productionOrderCostService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
