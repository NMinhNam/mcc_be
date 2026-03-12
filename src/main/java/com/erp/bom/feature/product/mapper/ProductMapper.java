package com.erp.bom.feature.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.bom.feature.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
