package com.erp.bom.feature.production.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.production.entity.ProductionOrder;
import com.erp.bom.feature.production.service.ProductionOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/production-orders")
public class ProductionOrderController {

    private static final Logger log = LoggerFactory.getLogger(ProductionOrderController.class);

    private final ProductionOrderService productionOrderService;

    public ProductionOrderController(ProductionOrderService productionOrderService) {
        this.productionOrderService = productionOrderService;
    }

    @PostMapping
    public ResponseEntity<ProductionOrder> create(@RequestBody ProductionOrder productionOrder) {
        log.info("[POST /api/production-orders] Payload: {}", productionOrder);
        ProductionOrder created = productionOrderService.create(productionOrder);
        log.info("[POST /api/production-orders] Response: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionOrder> getById(@PathVariable Long id) {
        log.info("[GET /api/production-orders/{}]", id);
        ProductionOrder productionOrder = productionOrderService.getById(id);
        if (productionOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productionOrder);
    }

    @GetMapping
    public ResponseEntity<List<ProductionOrder>> getAll() {
        log.info("[GET /api/production-orders]");
        return ResponseEntity.ok(productionOrderService.getAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ProductionOrder>> getPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        log.info("[GET /api/production-orders/page] page={}, size={}, status={}", page, size, status);
        return ResponseEntity.ok(productionOrderService.getPage(page, size, status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionOrder> update(@PathVariable Long id, @RequestBody ProductionOrder productionOrder) {
        log.info("[PUT /api/production-orders/{}] Payload: {}", id, productionOrder);
        ProductionOrder updated = productionOrderService.update(id, productionOrder);
        log.info("[PUT /api/production-orders/{}] Response: {}", id, updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("[DELETE /api/production-orders/{}]", id);
        productionOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProductionOrder> updateStatus(@PathVariable Long id, @RequestParam String status) {
        log.info("[PUT /api/production-orders/{}/status] status={}", id, status);
        ProductionOrder updated = productionOrderService.updateStatus(id, status);
        log.info("[PUT /api/production-orders/{}/status] Response: {}", id, updated);
        return ResponseEntity.ok(updated);
    }
}
