package com.erp.bom.feature.product.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductExcelResponse {

    @ExcelProperty("STT")
    private Integer no;

    @ExcelProperty("Nhóm")
    private String groupId;

    @ExcelProperty("Tên chi tiết")
    private String name;

    @ExcelProperty("Vật liệu")
    private String material;

    @ExcelProperty("T (mm)")
    private BigDecimal thickness;

    @ExcelProperty("W (mm)")
    private BigDecimal width;

    @ExcelProperty("L (mm)")
    private BigDecimal length;

    @ExcelProperty("PCS")
    private Integer pcs;

    @ExcelProperty("Hệ số")
    private BigDecimal multiplier;

    @ExcelProperty("Net (m³)")
    private BigDecimal volumeNet;

    @ExcelProperty("Raw (m³)")
    private BigDecimal volumeRaw;

    @ExcelProperty("Ốc vít")
    private Integer boltQuantity;

    @ExcelProperty("Đơn giá")
    private BigDecimal unitPrice;

    @ExcelProperty("Tổng cộng")
    private BigDecimal totalPrice;
}