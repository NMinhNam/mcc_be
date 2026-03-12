package com.erp.bom.feature.production.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.bom.feature.production.entity.ProductionOrderComponent;

import java.util.List;

public interface ProductionOrderComponentService extends IService<ProductionOrderComponent> {
    List<ProductionOrderComponent> getByProductionOrderId(Long productionOrderId);
}
