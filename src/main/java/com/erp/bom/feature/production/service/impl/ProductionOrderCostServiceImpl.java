package com.erp.bom.feature.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.bom.feature.production.entity.ProductionOrderCost;
import com.erp.bom.feature.production.mapper.ProductionOrderCostMapper;
import com.erp.bom.feature.production.service.ProductionOrderCostService;
import org.springframework.stereotype.Service;

@Service
public class ProductionOrderCostServiceImpl extends ServiceImpl<ProductionOrderCostMapper, ProductionOrderCost> implements ProductionOrderCostService {

    @Override
    public ProductionOrderCost getByProductionOrderId(Long productionOrderId) {
        return lambdaQuery()
                .eq(ProductionOrderCost::getProductionOrderId, productionOrderId)
                .one();
    }
}
