package com.erp.bom.feature.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.erp.bom.feature.product.dto.BomRowRequest;
import com.erp.bom.feature.product.dto.BomRowResponse;
import com.erp.bom.feature.product.entity.BomRow;
import com.erp.bom.feature.product.mapper.BomRowMapper;
import com.erp.bom.feature.product.service.BomRowService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BomRowServiceImpl implements BomRowService {

    private final BomRowMapper bomRowMapper;

    public BomRowServiceImpl(BomRowMapper bomRowMapper) {
        this.bomRowMapper = bomRowMapper;
    }

    @Override
    public List<BomRowResponse> getBomRowsByProductId(UUID productId) {
        LambdaQueryWrapper<BomRow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomRow::getProductId, productId)
                .orderByAsc(BomRow::getStt);

        List<BomRow> rows = bomRowMapper.selectList(wrapper);
        return toResponseList(rows);
    }

    @Override
    public List<BomRow> getBomRowEntitiesByProductId(UUID productId) {
        LambdaQueryWrapper<BomRow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomRow::getProductId, productId)
                .orderByAsc(BomRow::getStt);

        return bomRowMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public void syncBomRows(UUID productId, List<BomRowRequest> requests) {
        LambdaQueryWrapper<BomRow> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(BomRow::getProductId, productId);
        bomRowMapper.delete(deleteWrapper);

        for (BomRowRequest request : requests) {
            BomRow entity = toEntity(request, productId);
            bomRowMapper.insert(entity);
        }
    }

    @Override
    @Transactional
    public void importBom(UUID productId, MultipartFile file) throws IOException {
        List<BomRowRequest> requests = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                BomRowRequest request = new BomRowRequest();
                request.setStt(i);
                request.setProductId(productId);

                Cell nameCell = row.getCell(0);
                if (nameCell != null) request.setName(dataFormatter.formatCellValue(nameCell));
                else request.setName("Unknown");

                Cell materialCell = row.getCell(1);
                if (materialCell != null) request.setMaterial(dataFormatter.formatCellValue(materialCell));

                Cell thicknessCell = row.getCell(2);
                if (thicknessCell != null) {
                    try {
                        request.setThickness(new BigDecimal(dataFormatter.formatCellValue(thicknessCell)));
                    } catch (Exception ignored) {
                    }
                }

                Cell widthCell = row.getCell(3);
                if (widthCell != null) {
                    try {
                        request.setWidth(new BigDecimal(dataFormatter.formatCellValue(widthCell)));
                    } catch (Exception ignored) {
                    }
                }

                Cell lengthCell = row.getCell(4);
                if (lengthCell != null) {
                    try {
                        request.setLength(new BigDecimal(dataFormatter.formatCellValue(lengthCell)));
                    } catch (Exception ignored) {
                    }
                }

                Cell pcsCell = row.getCell(5);
                if (pcsCell != null) {
                    try {
                        request.setPcs(Integer.parseInt(dataFormatter.formatCellValue(pcsCell)));
                    } catch (Exception ignored) {
                    }
                }

                requests.add(request);
            }
        }
        syncBomRows(productId, requests);
    }

    @Override
    public void exportBom(UUID productId, HttpServletResponse response) throws IOException {
        List<BomRowResponse> rows = getBomRowsByProductId(productId);
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("BOM");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("STT");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Material");
            header.createCell(3).setCellValue("Thickness");
            header.createCell(4).setCellValue("Width");
            header.createCell(5).setCellValue("Length");
            header.createCell(6).setCellValue("Pcs");

            int rowIdx = 1;
            for (BomRowResponse r : rows) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(r.getStt() != null ? r.getStt() : 0);
                row.createCell(1).setCellValue(r.getName() != null ? r.getName() : "");
                row.createCell(2).setCellValue(r.getMaterial() != null ? r.getMaterial() : "");
                row.createCell(3).setCellValue(r.getThickness() != null ? r.getThickness().doubleValue() : 0);
                row.createCell(4).setCellValue(r.getWidth() != null ? r.getWidth().doubleValue() : 0);
                row.createCell(5).setCellValue(r.getLength() != null ? r.getLength().doubleValue() : 0);
                row.createCell(6).setCellValue(r.getPcs() != null ? r.getPcs() : 0);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"bom_" + productId + ".xlsx\"");
            workbook.write(response.getOutputStream());
            response.flushBuffer();
        }
    }

    private BomRow toEntity(BomRowRequest request, UUID productId) {
        BomRow entity = new BomRow();
        if (request.getId() != null) {
            entity.setId(request.getId());
        }
        entity.setProductId(productId);
        entity.setStt(request.getStt());
        entity.setGroupId(request.getGroupId());
        entity.setName(request.getName());
        entity.setMaterial(request.getMaterial());
        entity.setThickness(request.getThickness());
        entity.setWidth(request.getWidth());
        entity.setLength(request.getLength());
        entity.setPcs(request.getPcs() != null ? request.getPcs() : 1);
        entity.setMultiplier(request.getMultiplier() != null ? request.getMultiplier() : java.math.BigDecimal.ONE);
        entity.setVolumeNet(request.getVolumeNet());
        entity.setVolumeRaw(request.getVolumeRaw());
        entity.setBoltQuantity(request.getBoltQuantity() != null ? request.getBoltQuantity() : 0);
        entity.setUnitPrice(request.getUnitPrice());
        entity.setCurrency(request.getCurrency() != null ? request.getCurrency() : "VND");
        entity.setTotalPrice(request.getTotalPrice());
        entity.setNote(request.getNote());
        return entity;
    }

    private BomRowResponse toResponse(BomRow entity) {
        BomRowResponse response = new BomRowResponse();
        response.setId(entity.getId());
        response.setStt(entity.getStt());
        response.setProductId(entity.getProductId());
        response.setGroupId(entity.getGroupId());
        response.setName(entity.getName());
        response.setMaterial(entity.getMaterial());
        response.setThickness(entity.getThickness());
        response.setWidth(entity.getWidth());
        response.setLength(entity.getLength());
        response.setPcs(entity.getPcs());
        response.setMultiplier(entity.getMultiplier());
        response.setVolumeNet(entity.getVolumeNet());
        response.setVolumeRaw(entity.getVolumeRaw());
        response.setBoltQuantity(entity.getBoltQuantity());
        response.setUnitPrice(entity.getUnitPrice());
        response.setCurrency(entity.getCurrency());
        response.setTotalPrice(entity.getTotalPrice());
        response.setNote(entity.getNote());
        return response;
    }

    @Override
    @Transactional
    public void deleteByProductId(UUID productId) {
        LambdaQueryWrapper<BomRow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomRow::getProductId, productId);
        bomRowMapper.delete(wrapper);
    }

    private List<BomRowResponse> toResponseList(List<BomRow> entities) {
        List<BomRowResponse> responses = new ArrayList<>();
        for (BomRow entity : entities) {
            responses.add(toResponse(entity));
        }
        return responses;
    }
}
