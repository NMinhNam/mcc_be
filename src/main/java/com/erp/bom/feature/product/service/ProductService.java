package com.erp.bom.feature.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.bom.feature.cloudinary.CloudinaryService;
import com.erp.bom.feature.product.dto.ProductExcelResponse;
import com.erp.bom.feature.product.dto.ProductHeaderResponse;
import com.erp.bom.feature.product.dto.BomTemplateRow;
import com.erp.bom.feature.product.entity.BomRow;
import com.erp.bom.feature.product.entity.Product;
import com.erp.bom.feature.product.mapper.BomRowMapper;
import com.erp.bom.feature.product.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;
    private final BomRowService bomRowService;
    private final BomRowMapper bomRowMapper;

    public ProductService(ProductMapper productMapper, CloudinaryService cloudinaryService, BomRowService bomRowService, BomRowMapper bomRowMapper) {
        this.productMapper = productMapper;
        this.cloudinaryService = cloudinaryService;
        this.bomRowService = bomRowService;
        this.bomRowMapper = bomRowMapper;
    }

    public Product create(Product product) {
        if (StringUtils.hasText(product.getImage()) && product.getImage().startsWith("data:image")) {
            try {
                String imageUrl = cloudinaryService.uploadFromBase64(product.getImage());
                product.setImage(imageUrl);
            } catch (IOException e) {
                log.error("Failed to upload image to Cloudinary: {}", e.getMessage());
            }
        }
        productMapper.insert(product);
        return product;
    }

    public Product createProduct(String code, String name, String note, String imageBase64, MultipartFile imageFile) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setNote(note);

        if (StringUtils.hasText(imageBase64) && imageBase64.startsWith("data:image")) {
            try {
                String imageUrl = cloudinaryService.uploadFromBase64(imageBase64);
                product.setImage(imageUrl);
            } catch (IOException e) {
                log.error("Failed to upload base64 image to Cloudinary: {}", e.getMessage());
            }
        } else if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.upload(imageFile);
                product.setImage(imageUrl);
            } catch (IOException e) {
                log.error("Failed to upload file image to Cloudinary: {}", e.getMessage());
            }
        }

        productMapper.insert(product);
        return product;
    }

    public Product getById(UUID id) {
        return productMapper.selectById(id);
    }

    public List<Product> getAll() {
        log.info("[GET /products]");
        var selectListProductResult = productMapper.selectList(null);
        if (selectListProductResult.isEmpty()) {
            log.info("Product list is empty");
        }
        log.info("List Product result: {}", selectListProductResult);
        return selectListProductResult;
    }

    public Page<Product> getPage(int page, int size, String search) {
        Page<Product> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(search)) {
            wrapper.like(Product::getName, search)
                    .or()
                    .like(Product::getCode, search);
        }
        wrapper.orderByDesc(Product::getId);
        return productMapper.selectPage(pageParam, wrapper);
    }

    public Product update(UUID id, Product product) {
        Product existing = productMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        product.setId(id);
        productMapper.updateById(product);
        return productMapper.selectById(id);
    }

    public void delete(UUID id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return;
        }

        if (StringUtils.hasText(product.getImage())) {
            try {
                cloudinaryService.delete(product.getImage());
                log.info("Deleted product image from Cloudinary: {}", product.getImage());
            } catch (IOException e) {
                log.error("Failed to delete product image from Cloudinary: {}", e.getMessage());
            }
        }

        bomRowService.deleteByProductId(id);

        productMapper.deleteById(id);
        log.info("Deleted product with id: {}", id);
    }

    public void exportAll(HttpServletResponse response) throws IOException {
        List<Product> products = productMapper.selectList(null);
        
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        WriteFont headFont = new WriteFont();
        headFont.setBold(true);
        headStyle.setWriteFont(headFont);

        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);

        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(headStyle, contentStyle);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=products_" + 
                System.currentTimeMillis() + ".xlsx");

        EasyExcel.write(response.getOutputStream(), Product.class)
                .head(createProductExcelHeader())
                .sheet("Products")
                .registerWriteHandler(styleStrategy)
                .doWrite(products);
    }

    public void exportWithBom(UUID productId, HttpServletResponse response) throws IOException {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + productId);
        }

        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BomRow> wrapper = 
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(BomRow::getProductId, productId)
                .orderByAsc(BomRow::getStt);
        List<BomRow> bomRows = bomRowMapper.selectList(wrapper);

        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        WriteFont headFont = new WriteFont();
        headFont.setBold(true);
        headStyle.setWriteFont(headFont);

        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);

        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(headStyle, contentStyle);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + product.getCode() + "_bom_" + 
                System.currentTimeMillis() + ".xlsx");

        AtomicInteger index = new AtomicInteger(1);
        List<ProductExcelResponse> bomData = bomRows.stream().map(row -> {
            ProductExcelResponse dto = new ProductExcelResponse();
            dto.setNo(index.getAndIncrement());
            dto.setGroupId(row.getGroupId());
            dto.setName(row.getName());
            dto.setMaterial(row.getMaterial());
            dto.setThickness(row.getThickness());
            dto.setWidth(row.getWidth());
            dto.setLength(row.getLength());
            dto.setPcs(row.getPcs());
            dto.setMultiplier(row.getMultiplier());
            dto.setVolumeNet(row.getVolumeNet());
            dto.setVolumeRaw(row.getVolumeRaw());
            dto.setBoltQuantity(row.getBoltQuantity());
            dto.setUnitPrice(row.getUnitPrice());
            dto.setTotalPrice(row.getTotalPrice());
            return dto;
        }).toList();

        product.setName(product.getName());
        
        EasyExcel.write(response.getOutputStream(), ProductExcelResponse.class)
                .sheet(product.getCode() + " - " + product.getName())
                .registerWriteHandler(styleStrategy)
                .doWrite(bomData);
    }

    private List<List<String>> createProductExcelHeader() {
        return List.of(
                List.of("ID"),
                List.of("Mã sản phẩm"),
                List.of("Tên sản phẩm"),
                List.of("Ghi chú"),
                List.of("Ngày tạo"),
                List.of("Ngày cập nhật")
        );
    }

    public void downloadBomTemplate(HttpServletResponse response) throws IOException {
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        WriteFont headFont = new WriteFont();
        headFont.setBold(true);
        headStyle.setWriteFont(headFont);

        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);

        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(headStyle, contentStyle);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=bom_template.xlsx");

        BomTemplateRow template = new BomTemplateRow();
        template.setStt(1);
        template.setGroupId("A");
        template.setName("Tên chi tiết mẫu");
        template.setMaterial("Tôn");
        template.setThickness(new BigDecimal("1.5"));
        template.setWidth(new BigDecimal("600"));
        template.setLength(new BigDecimal("800"));
        template.setPcs(2);
        template.setMultiplier(new BigDecimal("1.0"));
        template.setVolumeNet(new BigDecimal("0.0018"));
        template.setVolumeRaw(new BigDecimal("0.0020"));
        template.setBoltQuantity(8);
        template.setUnitPrice(new BigDecimal("15000"));
        template.setCurrency("VND");
        template.setTotalPrice(new BigDecimal("30000"));
        template.setNote("Ghi chú mẫu");

        EasyExcel.write(response.getOutputStream(), BomTemplateRow.class)
                .sheet("BOM Template")
                .head(createBomTemplateHeader())
                .registerWriteHandler(styleStrategy)
                .doWrite(List.of(template));
    }

    private List<List<String>> createBomTemplateHeader() {
        return List.of(
                List.of("STT"),
                List.of("Nhóm"),
                List.of("Tên chi tiết"),
                List.of("Vật liệu"),
                List.of("T (mm)"),
                List.of("W (mm)"),
                List.of("L (mm)"),
                List.of("PCS"),
                List.of("Hệ số"),
                List.of("Net (m³)"),
                List.of("Raw (m³)"),
                List.of("Ốc vít"),
                List.of("Đơn giá"),
                List.of("Tiền tệ"),
                List.of("Tổng cộng"),
                List.of("Ghi chú")
        );
    }
}
