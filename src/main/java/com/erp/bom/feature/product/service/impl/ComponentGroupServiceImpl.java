package com.erp.bom.feature.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.bom.feature.product.entity.ComponentGroup;
import com.erp.bom.feature.product.mapper.ComponentGroupMapper;
import com.erp.bom.feature.product.service.ComponentGroupService;
import org.springframework.stereotype.Service;

@Service
public class ComponentGroupServiceImpl extends ServiceImpl<ComponentGroupMapper, ComponentGroup> implements ComponentGroupService {
}
