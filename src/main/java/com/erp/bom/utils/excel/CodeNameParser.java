package com.erp.bom.utils.excel;

import java.util.HashMap;
import java.util.Map;

public class CodeNameParser {

    private CodeNameParser() {
    }

    /**
     * Parse format: "code - name" to extract code and name
     * Example: "2 - Cái" -> code: "2", name: "Cái"
     *
     * @param input the input string in format "code - name"
     * @return a map with keys "code" and "name", or null if invalid format
     */
    public static Map<String, String> parse(String input) {
        return ExcelUtil.parseCodeAndName(input);
    }

    /**
     * Get code from input string
     *
     * @param input the input string in format "code - name"
     * @return the code part, or null if invalid format
     */
    public static String parseCode(String input) {
        Map<String, String> result = parse(input);
        return result != null ? result.get("code") : null;
    }

    /**
     * Get name from input string
     *
     * @param input the input string in format "code - name"
     * @return the name part, or null if invalid format
     */
    public static String parseName(String input) {
        Map<String, String> result = parse(input);
        return result != null ? result.get("name") : null;
    }
}
