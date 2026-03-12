package com.erp.bom.feature.hardware.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.hardware.entity.HardwareItem;
import com.erp.bom.feature.hardware.mapper.HardwareItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class HardwareItemService {

    @Autowired
    private HardwareItemMapper hardwareItemMapper;

    public HardwareItem create(HardwareItem hardwareItem) {
        hardwareItemMapper.insert(hardwareItem);
        return hardwareItem;
    }

    public HardwareItem getById(Long id) {
        return hardwareItemMapper.selectById(id);
    }

    public List<HardwareItem> getAll() {
        return hardwareItemMapper.selectList(null);
    }

    public Page<HardwareItem> getPage(int page, int size, String search) {
        Page<HardwareItem> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<HardwareItem> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(search)) {
            wrapper.like(HardwareItem::getName, search);
        }
        wrapper.orderByDesc(HardwareItem::getId);
        return hardwareItemMapper.selectPage(pageParam, wrapper);
    }

    public HardwareItem update(Long id, HardwareItem hardwareItem) {
        HardwareItem existing = hardwareItemMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("HardwareItem not found with id: " + id);
        }
        hardwareItem.setId(id);
        hardwareItemMapper.updateById(hardwareItem);
        return hardwareItemMapper.selectById(id);
    }

    public void delete(Long id) {
        hardwareItemMapper.deleteById(id);
    }
}

