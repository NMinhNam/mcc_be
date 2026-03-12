package com.erp.bom.feature.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BomSyncRequest {

    @NotNull(message = "rows is required")
    @Valid
    private List<BomRowRequest> rows;

    public List<BomRowRequest> getRows() {
        return rows;
    }

    public void setRows(List<BomRowRequest> rows) {
        this.rows = rows;
    }
}
