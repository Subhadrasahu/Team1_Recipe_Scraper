package utils;

import driverManager.DriverFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {
    private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);

    //Eliminate	Add	To Add ( if not fully vegan)	Recipes to avoid	Optional recipe
    public static final String LFV_Eliminate = "Final list for LFV Elimination  - Eliminate";
    public static final String LFV_Add = "Final list for LFV Elimination  - Add";
    public static final String LFV_Add_Vegan = "Final list for LFV Elimination  - To Add ( if not fully vegan)";
    public static final String LFV_Recipes_avoid = "Final list for LFV Elimination  - Recipes to avoid";
    public static final String LFV_Optional_recipe = "Final list for LFV Elimination  - Optional recipe";

    //Eliminate	Add	Recipes to avoid	Food Processing
    public static final String LCHF_Eliminate = "Final list for LCHFElimination  - Eliminate";
    public static final String LCHF_Add = "Final list for LCHFElimination  - Add";
    public static final String LCHF_Recipe_To_Avoid = "Final list for LCHFElimination  - Recipes to avoid";
    public static final String LCHF_Food_Processing = "Final list for LCHFElimination  - Food Processing";

    public static final String ALLERGY_BONUS = "Filter -1 Allergies - Bonus Poi - Allergies (Bonus points)";

    public static Map<String, List<String>> readExcelData() {
        Map<String, List<String>> finalMap = new HashMap<>();

        try {
            Map<String, List<String>> mapLFV = readExcelData("Final list for LFV Elimination ", "src/test/resources/testData/IngredientsAndComorbidities-ScrapperHackathon.xlsx", "");
            Map<String, List<String>> mapLCHF = readExcelData("Final list for LCHFElimination ", "src/test/resources/testData/IngredientsAndComorbidities-ScrapperHackathon.xlsx", "");
            Map<String, List<String>> mapAllergies = readExcelData("Filter -1 Allergies - Bonus Poi", "src/test/resources/testData/IngredientsAndComorbidities-ScrapperHackathon.xlsx", "Allergies (Bonus points)");
            finalMap.putAll(mapAllergies);
            finalMap.putAll(mapLFV);
            finalMap.putAll(mapLCHF);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return finalMap;
    }
    private static Map<String, List<String>> readExcelData(String sheetName, String fileName, String explicitName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum();
        Map<Integer, List<String>> map = new HashMap<>();
        Map<Integer, String> kv = new HashMap<>();
        int headerMaxColCount = 0;
        for (int i = 1; i <= rowCount; i++) { // Start from row 1 (skip header)
            Row row = sheet.getRow(i);
            if (row != null) {
                int cellCount = row.getLastCellNum();
                if (cellCount == -1)
                    break;
                Object[] rowData = new Object[cellCount];
                for (int j = 0; j < cellCount; j++) {

                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        if (i == 1) {
                            headerMaxColCount = cellCount;
                            map.put(j, new ArrayList<String>());
                            kv.put(j, cell.toString());
                            if (!"".equalsIgnoreCase(explicitName)) {
                                kv.put(j, explicitName);
                                map.get(j).add(cell.toString());
                            }

                        } else {
                            if (!"".equalsIgnoreCase(cell.toString()) && j < headerMaxColCount) {
                                map.get(j).add(cell.toString());
                            }
                        }

                        rowData[j] = cell.toString();
                    } else {
                        rowData[j] = "";
                    }
                }
            }
        }
        workbook.close();
        fis.close();
        Map<String, List<String>> finalMap = new HashMap<>();

        for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
            finalMap.put(sheetName + " - " + kv.get(entry.getKey()), entry.getValue());

        }
        logger.info("finalMap"+finalMap.size());

        return finalMap;
    }
}
