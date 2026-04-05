package com.erp.bom.utils.excel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImportResponseUtil {

    private ImportResponseUtil() {
    }

    public static Map<String, Object> buildImportResponse(String message, Object data, List<?> errors) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", data);
        result.put("mess", message);

        if (errors != null && !errors.isEmpty()) {
            result.put("errors", groupErrorsByRowIndex(errors));
        }

        result.put("status", true);
        return result;
    }

    private static List<Map<String, Object>> groupErrorsByRowIndex(List<?> errors) {
        return errors.stream()
                .map(e -> (ImportError) e)
                .collect(Collectors.groupingBy(ImportError::getRowIndex, LinkedHashMap::new, Collectors.toList()))
                .values()
                .stream()
                .map(ImportResponseUtil::buildGroupedError)
                .toList();
    }

    private static Map<String, Object> buildGroupedError(List<ImportError> errorList) {
        Map<String, Object> grouped = new LinkedHashMap<>();
        ImportError first = errorList.get(0);
        grouped.put("row_index", first.getRowIndex());
        grouped.put("row_data", first.getRowData());
        grouped.put("row_error", errorList.stream()
                .map(e -> Map.of("error_code", e.getErrorCode(), "error_message", e.getErrorMessage()))
                .toList());
        return grouped;
    }
}
