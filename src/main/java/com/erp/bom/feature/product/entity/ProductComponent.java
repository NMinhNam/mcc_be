package com.erp.bom.feature.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@TableName(value = "product_components", schema = "erp_bom")
public class ProductComponent {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("product_id")
    private UUID productId;

    @TableField("group_id")
    private Long groupId;

    @TableField("material_id")
    private Long materialId;

    private String name;

    @TableField("thickness_mm")
    private BigDecimal thicknessMm;

    @TableField("width_mm")
    private BigDecimal widthMm;

    @TableField("length_mm")
    private BigDecimal lengthMm;

    @TableField("pcs_per_product")
    private BigDecimal pcsPerProduct;

    @TableField("waste_factor")
    private BigDecimal wasteFactor;

    private String note;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("is_extra_row")
    private Boolean isExtraRow;

    @TableField("created_at")
    private OffsetDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getThicknessMm() {
        return thicknessMm;
    }

    public void setThicknessMm(BigDecimal thicknessMm) {
        this.thicknessMm = thicknessMm;
    }

    public BigDecimal getWidthMm() {
        return widthMm;
    }

    public void setWidthMm(BigDecimal widthMm) {
        this.widthMm = widthMm;
    }

    public BigDecimal getLengthMm() {
        return lengthMm;
    }

    public void setLengthMm(BigDecimal lengthMm) {
        this.lengthMm = lengthMm;
    }

    public BigDecimal getPcsPerProduct() {
        return pcsPerProduct;
    }

    public void setPcsPerProduct(BigDecimal pcsPerProduct) {
        this.pcsPerProduct = pcsPerProduct;
    }

    public BigDecimal getWasteFactor() {
        return wasteFactor;
    }

    public void setWasteFactor(BigDecimal wasteFactor) {
        this.wasteFactor = wasteFactor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsExtraRow() {
        return isExtraRow;
    }

    public void setIsExtraRow(Boolean isExtraRow) {
        this.isExtraRow = isExtraRow;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
