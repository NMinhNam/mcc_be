package com.erp.bom.feature.material.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.material.entity.Material;
import com.erp.bom.feature.material.mapper.MaterialMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MaterialService {

    private final MaterialMapper materialMapper;

    public MaterialService(MaterialMapper materialMapper) {
        this.materialMapper = materialMapper;
    }

    public Material create(Material material) {
        materialMapper.insert(material);
        return material;
    }

    public Material getById(Long id) {
        return materialMapper.selectById(id);
    }

    public List<Material> getAll() {
        return materialMapper.selectList(null);
    }

    public Page<Material> getPage(int page, int size, String search, Long materialTypeId) {
        Page<Material> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(search)) {
            wrapper.like(Material::getName, search);
        }
        if (materialTypeId != null) {
            wrapper.eq(Material::getMaterialTypeId, materialTypeId);
        }
        wrapper.orderByDesc(Material::getId);
        return materialMapper.selectPage(pageParam, wrapper);
    }

    public Material update(Long id, Material material) {
        Material existing = materialMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("Material not found with id: " + id);
        }
        material.setId(id);
        materialMapper.updateById(material);
        return materialMapper.selectById(id);
    }

    public void delete(Long id) {
        materialMapper.deleteById(id);
    }
}

