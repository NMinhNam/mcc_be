package com.erp.bom.feature.product.controller;

import com.erp.bom.feature.product.entity.ComponentGroup;
import com.erp.bom.feature.product.service.ComponentGroupService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/component-groups")
public class ComponentGroupController {

    @Autowired
    private ComponentGroupService componentGroupService;

    @PostMapping
    public ResponseEntity<ComponentGroup> create(@RequestBody ComponentGroup componentGroup) {
        componentGroupService.save(componentGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(componentGroup);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponentGroup> getById(@PathVariable Long id) {
        ComponentGroup componentGroup = componentGroupService.getById(id);
        if (componentGroup == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(componentGroup);
    }

    @GetMapping
    public ResponseEntity<List<ComponentGroup>> getAll() {
        return ResponseEntity.ok(componentGroupService.list());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ComponentGroup>> getByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(componentGroupService.lambdaQuery()
                .eq(ComponentGroup::getProductId, productId)
                .orderByAsc(ComponentGroup::getSortOrder)
                .list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponentGroup> update(@PathVariable Long id, @RequestBody ComponentGroup componentGroup) {
        componentGroup.setId(id);
        componentGroupService.updateById(componentGroup);
        return ResponseEntity.ok(componentGroupService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        componentGroupService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
