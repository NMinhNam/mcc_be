package com.erp.bom.feature.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.UUID;

@TableName(value = "product_hardware", schema = "erp_bom")
public class ProductHardware {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("product_id")
    private UUID productId;

    @TableField("hardware_item_id")
    private Long hardwareItemId;

    @TableField("qty_per_product")
    private BigDecimal qtyPerProduct;

    private String note;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
