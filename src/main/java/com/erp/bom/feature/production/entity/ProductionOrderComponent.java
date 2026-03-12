package com.erp.bom.feature.production.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName(value = "production_order_components", schema = "erp_bom")
public class ProductionOrderComponent {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("production_order_id")
    private Long productionOrderId;

    @TableField("product_component_id")
    private Long productComponentId;

    @TableField("thickness_mm")
    private BigDecimal thicknessMm;

    @TableField("width_mm")
    private BigDecimal widthMm;

    @TableField("length_mm")
    private BigDecimal lengthMm;

    @TableField("pcs_per_product")
    private BigDecimal pcsPerProduct;

    @TableField("result_pcs")
    private BigDecimal resultPcs;

    @TableField("volume_net_m3")
    private BigDecimal volumeNetM3;

    @TableField("volume_gross_m3")
    private BigDecimal volumeGrossM3;

    @TableField("total_volume_net_m3")
    private BigDecimal totalVolumeNetM3;

    @TableField("total_volume_gross_m3")
    private BigDecimal totalVolumeGrossM3;

    @TableField("material_price")
    private BigDecimal materialPrice;

    @TableField("material_cost")
    private BigDecimal materialCost;
}

