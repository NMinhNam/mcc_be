package com.erp.bom.feature.production.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.bom.feature.production.entity.ProductionOrderCost;

public interface ProductionOrderCostService extends IService<ProductionOrderCost> {
    ProductionOrderCost getByProductionOrderId(Long productionOrderId);
}
