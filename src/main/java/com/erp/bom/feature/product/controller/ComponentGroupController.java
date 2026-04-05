package com.erp.bom.feature.product.controller;

import com.erp.bom.feature.product.entity.ComponentGroup;
import com.erp.bom.feature.product.service.ComponentGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/component-groups")
public class ComponentGroupController {

    private final ComponentGroupService componentGroupService;

    public ComponentGroupController(ComponentGroupService componentGroupService) {
        this.componentGroupService = componentGroupService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ComponentGroup>> getByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(componentGroupService.lambdaQuery()
                .eq(ComponentGroup::getProductId, productId)
                .orderByAsc(ComponentGroup::getSortOrder)
                .list());
    }
}
