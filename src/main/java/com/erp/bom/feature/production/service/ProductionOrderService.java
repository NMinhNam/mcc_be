package com.erp.bom.feature.production.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.production.entity.ProductionOrder;
import com.erp.bom.feature.production.mapper.ProductionOrderMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProductionOrderService {

    private final ProductionOrderMapper productionOrderMapper;

    public ProductionOrderService(ProductionOrderMapper productionOrderMapper) {
        this.productionOrderMapper = productionOrderMapper;
    }

    public ProductionOrder create(ProductionOrder productionOrder) {
        String orderNo = generateOrderNo();
        productionOrder.setOrderNo(orderNo);
        if (productionOrder.getStatus() == null) {
            productionOrder.setStatus("pending");
        }
        productionOrderMapper.insert(productionOrder);
        return productionOrder;
    }

    public ProductionOrder getById(Long id) {
        return productionOrderMapper.selectById(id);
    }

    public List<ProductionOrder> getAll() {
        return productionOrderMapper.selectList(null);
    }

    public Page<ProductionOrder> getPage(int page, int size, String status) {
        Page<ProductionOrder> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ProductionOrder> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(ProductionOrder::getStatus, status);
        }
        wrapper.orderByDesc(ProductionOrder::getId);
        return productionOrderMapper.selectPage(pageParam, wrapper);
    }

    public ProductionOrder update(Long id, ProductionOrder productionOrder) {
        ProductionOrder existing = productionOrderMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("ProductionOrder not found with id: " + id);
        }
        productionOrder.setId(id);
        productionOrderMapper.updateById(productionOrder);
        return productionOrderMapper.selectById(id);
    }

    public void delete(Long id) {
        productionOrderMapper.deleteById(id);
    }

    public ProductionOrder updateStatus(Long id, String status) {
        ProductionOrder order = productionOrderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("ProductionOrder not found with id: " + id);
        }
        order.setStatus(status);
        productionOrderMapper.updateById(order);
        return order;
    }

    private String generateOrderNo() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LambdaQueryWrapper<ProductionOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(ProductionOrder::getOrderNo, "PO-" + datePrefix)
                .orderByDesc(ProductionOrder::getId)
                .last("LIMIT 1");

        ProductionOrder lastOrder = productionOrderMapper.selectOne(wrapper);
        int sequence = 1;

        if (lastOrder != null) {
            String lastNo = lastOrder.getOrderNo();
            String[] parts = lastNo.split("-");
            if (parts.length == 3) {
                sequence = Integer.parseInt(parts[2]) + 1;
            }
        }

        return String.format("PO-%s-%04d", datePrefix, sequence);
    }
}