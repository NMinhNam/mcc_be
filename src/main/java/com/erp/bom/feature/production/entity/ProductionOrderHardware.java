package com.erp.bom.feature.production.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@TableName(value = "production_order_hardware", schema = "erp_bom")
public class ProductionOrderHardware {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("production_order_id")
    private Long productionOrderId;

    @TableField("hardware_item_id")
    private Long hardwareItemId;

    @TableField("qty_per_product")
    private BigDecimal qtyPerProduct;

    @TableField("total_qty")
    private BigDecimal totalQty;

    @TableField("unit_price")
    private BigDecimal unitPrice;

    @TableField("total_cost")
    private BigDecimal totalCost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductionOrderId() {
        return productionOrderId;
    }

    public void setProductionOrderId(Long productionOrderId) {
        this.productionOrderId = productionOrderId;
    }

    public Long getHardwareItemId() {
        return hardwareItemId;
    }

    public void setHardwareItemId(Long hardwareItemId) {
        this.hardwareItemId = hardwareItemId;
    }

    public BigDecimal getQtyPerProduct() {
        return qtyPerProduct;
    }

    public void setQtyPerProduct(BigDecimal qtyPerProduct) {
        this.qtyPerProduct = qtyPerProduct;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}

