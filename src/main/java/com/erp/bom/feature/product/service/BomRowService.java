package com.erp.bom.feature.product.service;

import com.erp.bom.feature.product.dto.BomRowRequest;
import com.erp.bom.feature.product.dto.BomRowResponse;
import com.erp.bom.feature.product.entity.BomRow;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BomRowService {

    List<BomRowResponse> getBomRowsByProductId(UUID productId);

    List<BomRow> getBomRowEntitiesByProductId(UUID productId);

    void syncBomRows(UUID productId, List<BomRowRequest> rows);

    void importBom(UUID productId, MultipartFile file) throws IOException;

    void exportBom(UUID productId, HttpServletResponse response) throws IOException;

    void deleteByProductId(UUID productId);
}
