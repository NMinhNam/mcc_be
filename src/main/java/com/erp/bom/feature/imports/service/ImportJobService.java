package com.erp.bom.feature.imports.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.imports.entity.ImportJob;
import com.erp.bom.feature.imports.mapper.ImportJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportJobService {

    @Autowired
    private ImportJobMapper importJobMapper;

    public ImportJob create(ImportJob importJob) {
        importJobMapper.insert(importJob);
        return importJob;
    }

    public ImportJob getById(Long id) {
        return importJobMapper.selectById(id);
    }

    public List<ImportJob> getAll() {
        return importJobMapper.selectList(null);
    }

    public Page<ImportJob> getPage(int page, int size, String status) {
        Page<ImportJob> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ImportJob> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(ImportJob::getStatus, status);
        }
        wrapper.orderByDesc(ImportJob::getId);
        return importJobMapper.selectPage(pageParam, wrapper);
    }

    public ImportJob update(Long id, ImportJob importJob) {
        ImportJob existing = importJobMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("ImportJob not found with id: " + id);
        }
        importJob.setId(id);
        importJobMapper.updateById(importJob);
        return importJobMapper.selectById(id);
    }

    public void delete(Long id) {
        importJobMapper.deleteById(id);
    }
}

