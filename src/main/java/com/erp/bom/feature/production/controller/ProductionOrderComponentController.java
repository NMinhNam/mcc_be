package com.erp.bom.feature.production.controller;

import com.erp.bom.feature.production.entity.ProductionOrderComponent;
import com.erp.bom.feature.production.service.ProductionOrderComponentService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/production-order-components")
public class ProductionOrderComponentController {

    private static final Logger log = LoggerFactory.getLogger(ProductionOrderComponentController.class);

    @Autowired
    private ProductionOrderComponentService productionOrderComponentService;

    @PostMapping
    public ResponseEntity<ProductionOrderComponent> create(@RequestBody ProductionOrderComponent component) {
        // Debug: Log raw request info
        log.debug("[POST /api/production-order-components] Request received");

        if (component == null) {
            log.error("[POST /api/production-order-components] Request body is null");
            return ResponseEntity.badRequest().build();
        }

        log.info("[POST /api/production-order-components] Payload: {}", component);

        if (component.getProductionOrderId() == null) {
            log.warn("[POST /api/production-order-components] productionOrderId is null");
            return ResponseEntity.badRequest().build();
        }
        if (component.getProductComponentId() == null) {
            log.warn("[POST /api/production-order-components] productComponentId is null");
            return ResponseEntity.badRequest().build();
        }

        productionOrderComponentService.save(component);
        log.info("[POST /api/production-order-components] Response: {}", component);
        return ResponseEntity.status(HttpStatus.CREATED).body(component);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionOrderComponent> getById(@PathVariable Long id) {
        log.info("[GET /api/production-order-components/{}]", id);
        ProductionOrderComponent component = productionOrderComponentService.getById(id);
        if (component == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(component);
    }

    @GetMapping
    public ResponseEntity<List<ProductionOrderComponent>> getAll() {
        log.info("[GET /api/production-order-components]");
        return ResponseEntity.ok(productionOrderComponentService.list());
    }

    @GetMapping("/order/{productionOrderId}")
    public ResponseEntity<List<ProductionOrderComponent>> getByProductionOrderId(@PathVariable Long productionOrderId) {
        log.info("[GET /api/production-order-components/order/{}]", productionOrderId);
        return ResponseEntity.ok(productionOrderComponentService.getByProductionOrderId(productionOrderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionOrderComponent> update(@PathVariable Long id, @RequestBody ProductionOrderComponent component) {
        log.info("[PUT /api/production-order-components/{}] Payload: {}", id, component);
        component.setId(id);
        productionOrderComponentService.updateById(component);
        return ResponseEntity.ok(productionOrderComponentService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("[DELETE /api/production-order-components/{}]", id);
        productionOrderComponentService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
