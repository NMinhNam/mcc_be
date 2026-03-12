package com.erp.bom.feature.production.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@TableName(value = "production_order_costs", schema = "erp_bom")
public class ProductionOrderCost {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("production_order_id")
    private Long productionOrderId;

    @TableField("material_total")
    private BigDecimal materialTotal;

    @TableField("hardware_total")
    private BigDecimal hardwareTotal;

    @TableField("other_cost")
    private BigDecimal otherCost;

    @TableField("grand_total_vnd")
    private BigDecimal grandTotalVnd;

    @TableField("grand_total_usd")
    private BigDecimal grandTotalUsd;

    @TableField("sell_price_vnd")
    private BigDecimal sellPriceVnd;

    @TableField("sell_price_usd")
    private BigDecimal sellPriceUsd;

    @TableField("profit_vnd")
    private BigDecimal profitVnd;

    @TableField("profit_usd")
    private BigDecimal profitUsd;

    @TableField("created_at")
    private OffsetDateTime createdAt;
}

