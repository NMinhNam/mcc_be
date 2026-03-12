package com.erp.bom.feature.product.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class BomRowRequest {

    private UUID id;
    private UUID productId;
    private Long productionOrderId;
    @NotNull(message = "stt is required")
    private Integer stt;

    private String groupId;

    @NotNull(message = "name is required")
    private String name;

    private String material;

    private BigDecimal thickness;

    private BigDecimal width;

    private BigDecimal length;

    private Integer pcs;

    private BigDecimal multiplier;

    private BigDecimal volumeNet;

    private BigDecimal volumeRaw;

    private Integer boltQuantity;

    private BigDecimal unitPrice;

    private String currency;

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

    public Long getProductionOrderId() {
        return productionOrderId;
    }

    public void setProductionOrderId(Long productionOrderId) {
        this.productionOrderId = productionOrderId;
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
