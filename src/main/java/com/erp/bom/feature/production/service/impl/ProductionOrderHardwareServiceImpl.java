package com.erp.bom.feature.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.bom.feature.production.entity.ProductionOrderHardware;
import com.erp.bom.feature.production.mapper.ProductionOrderHardwareMapper;
import com.erp.bom.feature.production.service.ProductionOrderHardwareService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionOrderHardwareServiceImpl extends ServiceImpl<ProductionOrderHardwareMapper, ProductionOrderHardware> implements ProductionOrderHardwareService {

    @Override
    public List<ProductionOrderHardware> getByProductionOrderId(Long productionOrderId) {
        return lambdaQuery()
                .eq(ProductionOrderHardware::getProductionOrderId, productionOrderId)
                .orderByAsc(ProductionOrderHardware::getId)
                .list();
    }
}
