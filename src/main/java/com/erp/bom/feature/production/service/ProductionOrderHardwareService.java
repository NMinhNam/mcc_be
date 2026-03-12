package com.erp.bom.feature.production.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.bom.feature.production.entity.ProductionOrderHardware;

import java.util.List;

public interface ProductionOrderHardwareService extends IService<ProductionOrderHardware> {
    List<ProductionOrderHardware> getByProductionOrderId(Long productionOrderId);
}
