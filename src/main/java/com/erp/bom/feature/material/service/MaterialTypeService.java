package com.erp.bom.feature.material.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.material.entity.MaterialType;
import com.erp.bom.feature.material.mapper.MaterialTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialTypeService {

    private final MaterialTypeMapper materialTypeMapper;

    public MaterialTypeService(MaterialTypeMapper materialTypeMapper) {
        this.materialTypeMapper = materialTypeMapper;
    }

    public MaterialType create(MaterialType materialType) {
        materialTypeMapper.insert(materialType);
        return materialType;
    }

    public MaterialType getById(Long id) {
        return materialTypeMapper.selectById(id);
    }

    public List<MaterialType> getAll() {
        return materialTypeMapper.selectList(null);
    }

    public Page<MaterialType> getPage(int page, int size) {
        Page<MaterialType> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<MaterialType> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(MaterialType::getId);
        return materialTypeMapper.selectPage(pageParam, wrapper);
    }

    public MaterialType update(Long id, MaterialType materialType) {
        MaterialType existing = materialTypeMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("MaterialType not found with id: " + id);
        }
        materialType.setId(id);
        materialTypeMapper.updateById(materialType);
        return materialTypeMapper.selectById(id);
    }

    public void delete(Long id) {
        materialTypeMapper.deleteById(id);
    }
}

