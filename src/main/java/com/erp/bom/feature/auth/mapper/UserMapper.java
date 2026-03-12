package com.erp.bom.feature.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.bom.feature.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
