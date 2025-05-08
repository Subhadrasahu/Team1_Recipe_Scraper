package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelReader {

    public static Map<String, List<List<String>>> readAllSheets(String filePath) {
        Map<String, List<List<String>>> allSheetData = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = filePath.endsWith(".xlsx") ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);
            int numberOfSheets = workbook.getNumberOfSheets();

            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                List<List<String>> sheetData = new ArrayList<>();

                for (Row row : sheet) {
                    List<String> rowData = new ArrayList<>();
                    for (Cell cell : row) {
                        rowData.add(getCellValueAsString(cell));
                    }
                    sheetData.add(rowData);
                }

                allSheetData.put(sheet.getSheetName(), sheetData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allSheetData;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }
}