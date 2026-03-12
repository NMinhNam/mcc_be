package com.erp.bom.feature.production.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@TableName(value = "production_orders", schema = "erp_bom")
public class ProductionOrder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_no")
    private String orderNo;

    @TableField("product_id")
    private UUID productId;

    @TableField("order_quantity")
    private BigDecimal orderQuantity;

    @TableField("redo_quantity")
    private BigDecimal redoQuantity;

    private String status;

    private String note;

    @TableField("created_at")
    private OffsetDateTime createdAt;
}

