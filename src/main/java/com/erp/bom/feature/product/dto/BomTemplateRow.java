package com.erp.bom.feature.product.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(25)
public class BomTemplateRow {

    @ExcelProperty(value = "STT", index = 0)
    @ColumnWidth(10)
    private Integer stt;

    @ExcelProperty(value = "Nhóm", index = 1)
    @ColumnWidth(15)
    private String groupId;

    @ExcelProperty(value = "Tên chi tiết", index = 2)
    @ColumnWidth(30)
    private String name;

    @ExcelProperty(value = "Vật liệu", index = 3)
    @ColumnWidth(20)
    private String material;

    @ExcelProperty(value = "T (mm)", index = 4)
    @ColumnWidth(12)
    private BigDecimal thickness;

    @ExcelProperty(value = "W (mm)", index = 5)
    @ColumnWidth(12)
    private BigDecimal width;

    @ExcelProperty(value = "L (mm)", index = 6)
    @ColumnWidth(12)
    private BigDecimal length;

    @ExcelProperty(value = "PCS", index = 7)
    @ColumnWidth(10)
    private Integer pcs;

    @ExcelProperty(value = "Hệ số", index = 8)
    @ColumnWidth(12)
    private BigDecimal multiplier;

    @ExcelProperty(value = "Net (m³)", index = 9)
    @ColumnWidth(15)
    private BigDecimal volumeNet;

    @ExcelProperty(value = "Raw (m³)", index = 10)
    @ColumnWidth(15)
    private BigDecimal volumeRaw;

    @ExcelProperty(value = "Ốc vít", index = 11)
    @ColumnWidth(10)
    private Integer boltQuantity;

    @ExcelProperty(value = "Đơn giá", index = 12)
    @ColumnWidth(15)
    private BigDecimal unitPrice;

    @ExcelProperty(value = "Tiền tệ", index = 13)
    @ColumnWidth(12)
    private String currency;

    @ExcelProperty(value = "Tổng cộng", index = 14)
    @ColumnWidth(15)
    private BigDecimal totalPrice;

    @ExcelProperty(value = "Ghi chú", index = 15)
    @ColumnWidth(30)
    private String note;
}