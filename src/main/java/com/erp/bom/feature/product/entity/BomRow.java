package com.erp.bom.feature.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.UUID;

@TableName(value = "bom_rows", schema = "erp_bom")
public class BomRow {

    @TableId(value = "id", type = IdType.AUTO)
    private UUID id;

    @TableField("product_id")
    private UUID productId;

    private Integer stt;

    @TableField("group_id")
    private String groupId;

    private String name;

    private String material;

    private BigDecimal thickness;

    private BigDecimal width;

    private BigDecimal length;

    private Integer pcs;

    private BigDecimal multiplier;

    @TableField("volume_net")
    private BigDecimal volumeNet;

    @TableField("volume_raw")
    private BigDecimal volumeRaw;

    @TableField("bolt_quantity")
    private Integer boltQuantity;

    @TableField("unit_price")
    private BigDecimal unitPrice;

    private String currency;

    @TableField("total_price")
    private BigDecimal totalPrice;

    private String note;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getStt() {
        return stt;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public BigDecimal getThickness() {
        return thickness;
    }

    public void setThickness(BigDecimal thickness) {
        this.thickness = thickness;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public Integer getPcs() {
        return pcs;
    }

    public void setPcs(Integer pcs) {
        this.pcs = pcs;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public BigDecimal getVolumeNet() {
        return volumeNet;
    }

    public void setVolumeNet(BigDecimal volumeNet) {
        this.volumeNet = volumeNet;
    }

    public BigDecimal getVolumeRaw() {
        return volumeRaw;
    }

    public void setVolumeRaw(BigDecimal volumeRaw) {
        this.volumeRaw = volumeRaw;
    }

    public Integer getBoltQuantity() {
        return boltQuantity;
    }

    public void setBoltQuantity(Integer boltQuantity) {
        this.boltQuantity = boltQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
