package com.erp.bom.feature.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.bom.feature.production.entity.ProductionOrderComponent;
import com.erp.bom.feature.production.mapper.ProductionOrderComponentMapper;
import com.erp.bom.feature.production.service.ProductionOrderComponentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionOrderComponentServiceImpl extends ServiceImpl<ProductionOrderComponentMapper, ProductionOrderComponent> implements ProductionOrderComponentService {

    @Override
    public List<ProductionOrderComponent> getByProductionOrderId(Long productionOrderId) {
        return lambdaQuery()
                .eq(ProductionOrderComponent::getProductionOrderId, productionOrderId)
                .orderByAsc(ProductionOrderComponent::getId)
                .list();
    }
}
