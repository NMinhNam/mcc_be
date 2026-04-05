package com.erp.bom.utils.excel;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    private ExcelUtil() {}

    public static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024L; // 5MB
    private static final List<String> EXCEL_FILE_EXTENSION = Arrays.asList(".xlsx", ".xls");
    private static final String EXCEL_HEADER = "Content-Disposition";

    /**
     * Sets common HTTP headers for Excel file download.
     *
     * @param response    HttpServletResponse to set headers on.
     * @param fileName    The base name for the downloaded file (without extension).
     * @param contentType The Content-Type header value.
     */
    public static void setExcelResponseHeaders(HttpServletResponse response, String fileName, String contentType) {
        response.setContentType(contentType);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader(EXCEL_HEADER, "attachment;filename=" + encodedFileName + ".xlsx");
    }

    /**
     * Generates a default filename for Excel exports based on prefix and current timestamp.
     *
     * @param prefix The prefix for the filename (e.g., "Category_List").
     * @return A formatted filename string.
     */
    public static String generateExcelFileName(String prefix) {
        return prefix + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * Validates an uploaded Excel file.
     *
     * @param file MultipartFile to validate.
     * @return true if the file is valid, false otherwise.
     */
    public static boolean validateExcelFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }

        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if (EXCEL_FILE_EXTENSION.contains(extension)) {
            return false;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        return true;
    }

    public static HorizontalCellStyleStrategy styleStrategy() {
        // style cho header
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

        WriteFont headFont = new WriteFont();
        headFont.setBold(true);
        headFont.setColor(IndexedColors.BLACK.getIndex());
        headStyle.setWriteFont(headFont);

        // style cho content
        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);

        return new HorizontalCellStyleStrategy(headStyle, contentStyle);
    }

    public static Map<String, Object> commonHeader(String reportTitle) {
        Map<String, Object> headerVars = new HashMap<>();
        headerVars.put("companyName", "CÔNG TY CỔ PHẦN LẮP ĐẶT 247");
        headerVars.put("reportTitle", reportTitle);
        headerVars.put("exportedBy", "ADMIN");
        headerVars.put("exportedDate",
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

        return headerVars;
    }

    /**
     * Parse format: "code - name" to extract code and name
     * Example: "2 - Cái" -> code: "2", name: "Cái"
     *
     * @param input the input string in format "code - name"
     * @return a map with keys "code" and "name", or null if invalid format
     */
    public static Map<String, String> parseCodeAndName(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String trimmed = input.trim();
        int separatorIndex = trimmed.indexOf(" - ");

        if (separatorIndex == -1) {
            return null;
        }

        String code = trimmed.substring(0, separatorIndex).trim();
        String name = trimmed.substring(separatorIndex + 3).trim();

        if (code.isEmpty() || name.isEmpty()) {
            return null;
        }

        Map<String, String> result = new HashMap<>();
        result.put("code", code);
        result.put("name", name);
        return result;
    }

    /**
     * Get code from input string in format "code - name"
     *
     * @param input the input string in format "code - name"
     * @return the code part, or null if invalid format
     */
    public static String parseCode(String input) {
        Map<String, String> result = parseCodeAndName(input);
        return result != null ? result.get("code") : null;
    }

    /**
     * Get name from input string in format "code - name"
     *
     * @param input the input string in format "code - name"
     * @return the name part, or null if invalid format
     */
    public static String parseName(String input) {
        Map<String, String> result = parseCodeAndName(input);
        return result != null ? result.get("name") : null;
    }
}