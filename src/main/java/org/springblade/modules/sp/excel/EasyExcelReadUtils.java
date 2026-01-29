package org.springblade.modules.sp.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springblade.modules.sp.dto.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class EasyExcelReadUtils {

    public static List<LiberaryExcelDTO> readLiberaryExcel(InputStream inputStream) throws Exception {
        // 1. 读取 Excel 数据到字节数组
        byte[] excelData = readInputStreamToByteArray(inputStream);

        // 2. 使用 EasyExcel 读取数据到临时 List
        List<LiberaryExcelDTO> tempDataList = new ArrayList<>();
        try (InputStream easyExcelInputStream = new ByteArrayInputStream(excelData)) {
            EasyExcel.read(easyExcelInputStream, LiberaryExcelDTO.class, new ReadListener<LiberaryExcelDTO>() {
                @Override
                public void invoke(LiberaryExcelDTO data, AnalysisContext context) {
                    tempDataList.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().headRowNumber(1).doRead();
        }

        // 3. 使用 Apache POI 处理合并单元格
        try (InputStream poiInputStream = new ByteArrayInputStream(excelData)) {
            Workbook workbook = WorkbookFactory.create(poiInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, String> mergedCellValues = readMergedCells(sheet);

            // 4. 遍历数据，填充合并单元格的值
            for (int i = 0; i < tempDataList.size(); i++) {
                LiberaryExcelDTO data = tempDataList.get(i);
                
                // 业务参数组在第6列（索引为5）
                String parameters = mergedCellValues.getOrDefault((i + 1) + "_5", "");
                
                // 如果合并单元格没有值，尝试直接从当前行读取
                if (parameters == null || parameters.trim().isEmpty()) {
                    try {
                        Row row = sheet.getRow(i + 1); // +1 因为跳过表头
                        if (row != null) {
                            Cell cell = row.getCell(5); // 业务参数组列（索引5）
                            if (cell != null) {
                                parameters = getCellValue(cell);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("读取第" + (i + 1) + "行业务参数组失败: " + e.getMessage());
                        parameters = "";
                    }
                }
                
                data.setParameters(parameters != null ? parameters.trim() : "");
                
                // 调试信息（可删除）
                System.out.println("第" + (i + 1) + "行: 属性名称=" + data.getName() + ", 业务参数组=" + data.getParameters());
            }

            workbook.close();
        }

        return tempDataList;
    }

    public static List<EncodingLibraryExcelDTO> readEncodingLiberaryExcel(InputStream inputStream) throws Exception {
        // 1. 读取 Excel 数据到字节数组
        byte[] excelData = readInputStreamToByteArray(inputStream);

        // 2. 使用 EasyExcel 读取数据到临时 List
        List<EncodingLibraryExcelDTO> tempDataList = new ArrayList<>();
        try (InputStream easyExcelInputStream = new ByteArrayInputStream(excelData)) {
            EasyExcel.read(easyExcelInputStream, EncodingLibraryExcelDTO.class, new ReadListener<EncodingLibraryExcelDTO>() {
                @Override
                public void invoke(EncodingLibraryExcelDTO data, AnalysisContext context) {
                    tempDataList.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().headRowNumber(2).doRead();
        }
        // 3. 使用 Apache POI 处理合并单元格
//        try (InputStream poiInputStream = new ByteArrayInputStream(excelData)) {
//            Workbook workbook = WorkbookFactory.create(poiInputStream);
//            Sheet sheet = workbook.getSheetAt(0);
//            Map<String, String> mergedCellValues = readMergedCells(sheet);
//
//            // 4. 遍历数据，填充合并单元格的值
//            for (int i = 0; i < tempDataList.size(); i++) {
//                EncodingLibraryExcelDTO data = tempDataList.get(i);
//                String name = mergedCellValues.getOrDefault((i + 1) + "_0", "");
//                data.setName(name);
//            }
//
//            workbook.close();
//        }
        return tempDataList;
    }

    public static List<JSONObject> parseExcelToKeyValue(InputStream inputStream) throws Exception {
        byte[] excelData = readInputStreamToByteArray(inputStream);

        List<Map<Integer, String>> rawDataList = new ArrayList<>();
        try (InputStream easyExcelInputStream = new ByteArrayInputStream(excelData)) {
            EasyExcel.read(easyExcelInputStream, new ReadListener<Map<Integer, String>>() {
                @Override
                public void invoke(Map<Integer, String> data, AnalysisContext context) {
                    rawDataList.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().headRowNumber(0).doRead();
        }

        if (rawDataList.isEmpty()) {
            throw new Exception("Excel 文件为空或格式不正确");
        }

        Map<Integer, String> headerMap = rawDataList.remove(0); // 移除表头

        List<JSONObject> result = new ArrayList<>();
        for (Map<Integer, String> row : rawDataList) {
            JSONObject jsonRow = new JSONObject();
            for (Map.Entry<Integer, String> entry : row.entrySet()) {
                String header = headerMap.get(entry.getKey());
                if (header != null) {
                    jsonRow.put(header, entry.getValue());
                }
            }
            result.add(jsonRow);
        }

        return result;
    }

    public static List<ExcelHeaderAndData> readExcelWithMultiSheets(InputStream inputStream, ExcelTypeEnum excelType) throws Exception {
        // 将输入流转换为字节数组，避免多次读取流问题
        byte[] excelData = readInputStreamToByteArray(inputStream);

        List<ExcelHeaderAndData> allSheetsData = new ArrayList<>();

        // 使用一个新的 ByteArrayInputStream 进行读取
        try (InputStream tempInputStream = new ByteArrayInputStream(excelData)) {
            // 初始化 ExcelReader，显式指定文件类型
            ExcelReader excelReader = EasyExcel.read(tempInputStream)
                    .excelType(excelType) // 指定文件类型（XLS 或 XLSX）
                    .build();

            // 获取所有 Sheet 信息
            List<ReadSheet> sheets = excelReader.excelExecutor().sheetList();

            for (ReadSheet sheet : sheets) {
                // 为每个 Sheet 单独创建流
                try (InputStream sheetInputStream = new ByteArrayInputStream(excelData)) {
                    List<EncodingExcelDTO> excelDataList = new ArrayList<>();
                    Map<String, String> headerMap = new HashMap<>();

                    // 自定义监听器处理数据
                    ReadListener<EncodingExcelDTO> listener = new ReadListener<EncodingExcelDTO>() {
                        private Map<Integer, String> row1Map = new HashMap<>();
                        private Map<Integer, String> row2Map = new HashMap<>();
                        private int rowCount = 0;

                        @Override
                        public void invoke(EncodingExcelDTO data, AnalysisContext context) {
                            if (rowCount >= 4) { // 从第五行开始读取数据
                                excelDataList.add(data);
                            }
                            rowCount++;
                        }

                        @Override
                        public void invokeHead(Map<Integer, com.alibaba.excel.metadata.data.ReadCellData<?>> headMap, AnalysisContext context) {
                            if (rowCount == 0) {
                                headMap.forEach((key, value) -> row1Map.put(key, value.getStringValue()));
                            } else if (rowCount == 1) {
                                headMap.forEach((key, value) -> row2Map.put(key, value.getStringValue()));
                            }
                            rowCount++;
                        }

                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {
                            if (!row1Map.isEmpty() && !row2Map.isEmpty()) {
                                headerMap.put(row1Map.get(0), row1Map.get(1));
                                headerMap.put(row2Map.get(0), row2Map.get(1));
                            }
                        }
                    };

                    // 每个 Sheet 使用独立流进行读取
                    EasyExcel.read(sheetInputStream, EncodingExcelDTO.class, listener)
                            .sheet(sheet.getSheetNo())
                            .headRowNumber(4) // 从第 4 行开始读取表头
                            .doRead();

                    // 保存当前 Sheet 数据
                    allSheetsData.add(new ExcelHeaderAndData(headerMap, new ArrayList<>(excelDataList)));
                }
            }

            excelReader.finish(); // 关闭 ExcelReader
        } catch (Exception e) {
            throw new Exception("读取多 Sheet Excel 出错: " + e.getMessage(), e);
        }

        return allSheetsData;
    }
    // 方法：读取Excel文件，返回包含表头和数据的结果
    public static ExcelHeaderAndData readExcelWithHeader(InputStream inputStream) throws Exception {
        // 1. 读取 Excel 数据到字节数组
        byte[] excelData = readInputStreamToByteArray(inputStream);

        // 2. 使用 EasyExcel 读取数据到临时 List
        List<EncodingExcelDTO> excelDataList = new ArrayList<>();  // 存储实际的数据
        Map<Integer, String> row1Map = new HashMap<>();  // 存储第一行
        Map<Integer, String> row2Map = new HashMap<>();  // 存储第二行

        try (InputStream easyExcelInputStream = new ByteArrayInputStream(excelData)) {
            // 使用 EasyExcel 读取
            EasyExcel.read(easyExcelInputStream, EncodingExcelDTO.class, new ReadListener<EncodingExcelDTO>() {
                private int rowCount = 0;

                @Override
                public void invoke(EncodingExcelDTO data, AnalysisContext context) {
                    // 从第五行开始读取数据，将第四行视为表头
                    if (rowCount >= 4) {
                        excelDataList.add(data);
                    }
                    rowCount++;
                }

                @Override
                public void invokeHead(Map<Integer, com.alibaba.excel.metadata.data.ReadCellData<?>> headMap, AnalysisContext context) {
                    // 处理第1行 (rowCount = 0)
                    if (rowCount == 0) {
                        headMap.forEach((key, value) -> row1Map.put(key, value.getStringValue()));
                    }
                    // 处理第2行 (rowCount == 1)
                    else if (rowCount == 1) {
                        headMap.forEach((key, value) -> row2Map.put(key, value.getStringValue()));
                    }
                    rowCount++;
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().headRowNumber(4).doRead();  // 从第4行开始解析表头

            Map<String, String> headerMap = new HashMap<>();
            // 从 row1Map 和 row2Map 中获取key和value
            String keyForRow1 = row1Map.get(0);
            String valueForRow1 = row1Map.get(1);
            String keyForRow2 = row2Map.get(1);  // 假设第一列为key
            String valueForRow2 = row2Map.get(0);  // 假设第二列为value
            headerMap.put(keyForRow1, valueForRow1);
            headerMap.put(keyForRow2, valueForRow2);  // 添加 key-value 对

            // 返回一个包含header（第1-3行数据）和data（从第5行开始的实际数据）的实体类
            return new ExcelHeaderAndData(headerMap, excelDataList);
        } catch (Exception e) {
            throw new Exception("读取 Excel 出错: " + e.getMessage(), e);
        }
    }

    // 方法：获取Excel列的字母标识
    private static String getExcelColumnLabel(int columnIndex) {
        return String.valueOf((char) ('A' + columnIndex));
    }



    //    读取Wbs表格数据
public static List<WbsExcelDTO> readWbsExcel(InputStream inputStream) throws Exception {
    // 1. 读取 Excel 数据到字节数组
    byte[] excelData = readInputStreamToByteArray(inputStream);

    // 2. 使用 EasyExcel 读取数据到临时 List
    List<WbsExcelDTO> tempDataList = new ArrayList<>();
    try (InputStream easyExcelInputStream = new ByteArrayInputStream(excelData)) {
        EasyExcel.read(easyExcelInputStream, WbsExcelDTO.class, new ReadListener<WbsExcelDTO>() {
            @Override
            public void invoke(WbsExcelDTO data, AnalysisContext context) {
                tempDataList.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        }).sheet().headRowNumber(2).doRead();
    }

//     3. 使用 Apache POI 处理合并单元格
//    try (InputStream poiInputStream = new ByteArrayInputStream(excelData)) {
//        Workbook workbook = WorkbookFactory.create(poiInputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//        Map<String, String> mergedCellValues = readMergedCells(sheet);
//
//        // 4. 遍历数据，填充合并单元格的值
//        for (int i = 0; i < tempDataList.size(); i++) {
//            WbsExcelDTO data = tempDataList.get(i);
//            String parameters = mergedCellValues.getOrDefault((i + 1) + "_0", "");
//            data.setWbsName(parameters);
//        }
//
//        workbook.close();
//    }
    return tempDataList;
}

    //    读取Ebs表格数据
    public static List<EbsExcelDTO> readEbsExcel(InputStream inputStream) throws Exception {
        // 1. 读取 Excel 数据到字节数组
        byte[] excelData = readInputStreamToByteArray(inputStream);

        // 2. 使用 EasyExcel 读取数据到临时 List
        List<EbsExcelDTO> tempDataList = new ArrayList<>();
        try (InputStream easyExcelInputStream = new ByteArrayInputStream(excelData)) {
            EasyExcel.read(easyExcelInputStream, EbsExcelDTO.class, new ReadListener<EbsExcelDTO>() {
                @Override
                public void invoke(EbsExcelDTO data, AnalysisContext context) {
                    tempDataList.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().headRowNumber(2).doRead();
        }

//     3. 使用 Apache POI 处理合并单元格
//    try (InputStream poiInputStream = new ByteArrayInputStream(excelData)) {
//        Workbook workbook = WorkbookFactory.create(poiInputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//        Map<String, String> mergedCellValues = readMergedCells(sheet);
//
//        // 4. 遍历数据，填充合并单元格的值
//        for (int i = 0; i < tempDataList.size(); i++) {
//            WbsExcelDTO data = tempDataList.get(i);
//            String parameters = mergedCellValues.getOrDefault((i + 1) + "_0", "");
//            data.setWbsName(parameters);
//        }
//
//        workbook.close();
//    }
        return tempDataList;
    }

    /**
     * 读取LOI数据Excel文件，解析为指定格式的JSON数组
     * @param inputStream Excel文件输入流
     * @return 解析后的JSON数组，格式为[{"type":"类型名称", "name":["属性1","属性2"]}]
     * @throws Exception
     */
    public static List<Map<String, Object>> readLoiDataExcel(InputStream inputStream) throws Exception {
        // 1. 读取 Excel 数据到字节数组
        byte[] excelData = readInputStreamToByteArray(inputStream);

        List<Map<String, Object>> resultList = new ArrayList<>();

        // 2. 使用 Apache POI 处理Excel文件
        try (InputStream poiInputStream = new ByteArrayInputStream(excelData)) {
            Workbook workbook = WorkbookFactory.create(poiInputStream);

            // 判断Excel是哪种格式模板
            boolean isNewFormat = isUtf8LoiTemplate(workbook);
            
            if (isNewFormat) {
                // 解析新格式 UTF-8LOI模版.xlsx
                parseUtf8LoiTemplate(workbook, resultList);
            } else {
                // 原来的解析逻辑
                parseOriginalLoiTemplate(workbook, resultList);
            }

            workbook.close();
        }

        return resultList;
    }
    
    /**
     * 判断是否为UTF-8LOI模版格式
     * @param workbook Excel工作簿
     * @return 是否为新格式
     */
    private static boolean isUtf8LoiTemplate(Workbook workbook) {
        // 检查第一个sheet的第一行是否有"构件类型"标题
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) return false;
        
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) return false;
        
        // 检查第一行的单元格是否包含"构件类型"
        for (int i = 0; i <= headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String cellValue = getCellValue(cell);
                if ("构件类型".equals(cellValue)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 解析原始LOI模板格式
     * @param workbook Excel工作簿
     * @param resultList 结果列表
     */
    private static void parseOriginalLoiTemplate(Workbook workbook, List<Map<String, Object>> resultList) {
        // 遍历所有Sheet
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            // 获取合并单元格信息
            Map<String, String> mergedCellValues = readMergedCells(sheet);

            // 存储当前类型的数据
            Map<String, Object> currentType = null;
            List<String> currentNames = new ArrayList<>();
            String currentTypeName = "";

            // 遍历行数据
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                // 检查A列是否有类型名称（红色方框区域）
                String typeNameFromMerged = mergedCellValues.get(rowIndex + "_0");
                if (typeNameFromMerged != null && !typeNameFromMerged.trim().isEmpty() &&
                        !typeNameFromMerged.equals(currentTypeName)) {

                    // 保存上一个类型的数据
                    if (currentType != null && !currentNames.isEmpty()) {
                        currentType.put("name", new ArrayList<>(currentNames));
                        resultList.add(currentType);
                    }

                    // 开始新的类型
                    currentTypeName = typeNameFromMerged;
                    currentType = new HashMap<>();
                    currentType.put("type", currentTypeName);
                    currentNames = new ArrayList<>();
                }

                // 检查B列的属性名称（黄色方框区域）
                Cell attributeCell = row.getCell(1); // B列
                if (attributeCell != null) {
                    String attributeName = getCellValue(attributeCell).trim();
                    if (!attributeName.isEmpty() &&
                            !attributeName.equals("属性") &&
                            !attributeName.equals("序号") &&
                            !currentNames.contains(attributeName)) {
                        currentNames.add(attributeName);
                    }
                }
            }

            // 保存最后一个类型的数据
            if (currentType != null && !currentNames.isEmpty()) {
                currentType.put("name", new ArrayList<>(currentNames));
                resultList.add(currentType);
            }
        }
    }
    
    /**
     * 解析UTF-8LOI模板格式
     * @param workbook Excel工作簿
     * @param resultList 结果列表
     */
    private static void parseUtf8LoiTemplate(Workbook workbook, List<Map<String, Object>> resultList) {
        // 解析新格式的模板
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet == null) continue;

            // 查找表头行和对应的列索引
            int typeColIndex = -1;
            int nameColIndex = -1;
            Row headerRow = sheet.getRow(0);
            
            if (headerRow != null) {
                for (int i = 0; i <= headerRow.getLastCellNum(); i++) {
                    Cell cell = headerRow.getCell(i);
                    if (cell != null) {
                        String cellValue = getCellValue(cell);
                        if ("构件类型".equals(cellValue)) {
                            typeColIndex = i;
                        } else if ("属性名称".equals(cellValue)) {
                            nameColIndex = i;
                        }
                    }
                }
            }
            
            // 如果没有找到必要的列，跳过该sheet
            if (typeColIndex == -1 || nameColIndex == -1) {
                continue;
            }
            
            Map<String, Object> currentType = null;
            List<String> currentNames = new ArrayList<>();
            String currentTypeName = "";
            
            // 从第2行开始读取数据 (索引1)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                
                // 读取构件类型
                Cell typeCell = row.getCell(typeColIndex);
                String typeName = getCellValue(typeCell).trim();
                
                // 读取属性名称
                Cell nameCell = row.getCell(nameColIndex);
                String attributeName = getCellValue(nameCell).trim();
                
                // 如果类型不为空且与当前类型不同，则创建新的类型
                if (!typeName.isEmpty() && !typeName.equals(currentTypeName)) {
                    // 保存上一个类型的数据
                    if (currentType != null && !currentNames.isEmpty()) {
                        currentType.put("name", new ArrayList<>(currentNames));
                        resultList.add(currentType);
                    }
                    
                    // 开始新的类型
                    currentTypeName = typeName;
                    currentType = new HashMap<>();
                    currentType.put("type", currentTypeName);
                    currentNames = new ArrayList<>();
                }
                
                // 如果属性名称不为空且不重复，则添加到当前类型的属性列表中
                if (!attributeName.isEmpty() && !currentNames.contains(attributeName)) {
                    currentNames.add(attributeName);
                }
            }
            
            // 保存最后一个类型的数据
            if (currentType != null && !currentNames.isEmpty()) {
                currentType.put("name", new ArrayList<>(currentNames));
                resultList.add(currentType);
            }
        }
    }
    /**
     * 读取BOM表对比规则Excel文件的G-Q列第8行往后的内容
     * @param inputStream Excel文件输入流
     * @return 解析结果
     * @throws Exception
     */
    public static List<Map<String, Object>> readBomComparisonRules(InputStream inputStream) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();
        byte[] excelData = readInputStreamToByteArray(inputStream);

        try (InputStream poiInputStream = new ByteArrayInputStream(excelData)) {
            Workbook workbook = WorkbookFactory.create(poiInputStream);

            int numberOfSheets = workbook.getNumberOfSheets();
            System.out.println("Excel文件总共有 " + numberOfSheets + " 个sheet");

            if (numberOfSheets <= 1) {
                System.out.println("只有一个sheet，无法跳过第一张sheet");
                workbook.close();
                return resultList;
            }

            // 收集需要处理的sheet（跳过第一张）
            List<SheetData> sheetsToProcess = new ArrayList<>();
            for (int sheetIndex = 1; sheetIndex < numberOfSheets; sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                sheetsToProcess.add(new SheetData(sheet, sheetIndex));
            }

            System.out.println("将并行处理 " + sheetsToProcess.size() + " 个sheet");

            // 并行处理sheet
            List<Map<String, Object>> allResults = sheetsToProcess.parallelStream()
                    .map(sheetData -> {
                        try {
                            return readSheetData(sheetData.sheet, sheetData.sheetIndex);
                        } catch (Exception e) {
                            System.err.println("处理sheet[" + sheetData.sheetIndex + "] " + sheetData.sheet.getSheetName() + " 时出错: " + e.getMessage());
                            e.printStackTrace();
                            return new ArrayList<Map<String, Object>>();
                        }
                    })
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            resultList.addAll(allResults);
            System.out.println("并行处理完成，总共找到的elementId数量: " + resultList.size());

            workbook.close();
        }

        return resultList;
    }

    /**
     * 检查字符串是否为数字
     */
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Sheet数据封装类
     */
    private static class SheetData {
        final Sheet sheet;
        final int sheetIndex;

        SheetData(Sheet sheet, int sheetIndex) {
            this.sheet = sheet;
            this.sheetIndex = sheetIndex;
        }
    }
    /**
     * 读取单个sheet的数据
     */
    private static List<Map<String, Object>> readSheetData(Sheet sheet, int sheetIndex) {
        List<Map<String, Object>> sheetResultList = new ArrayList<>();
        String sheetName = sheet.getSheetName();

        System.out.println("开始处理sheet[" + sheetIndex + "]: " + sheetName + " (总行数: " + (sheet.getLastRowNum() + 1) + ")");

        // 从第8行开始读取（索引为7）
        for (int rowIndex = 7; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            Map<String, Object> rowData = new HashMap<>();

            // 读取G-Q列（索引6-16）
            for (int colIndex = 6; colIndex <= 16; colIndex++) {
                Cell cell = row.getCell(colIndex);
                String cellValue = getCellValue(cell);

                if (cellValue != null && !cellValue.trim().isEmpty()) {
                    // 检查是否为数字ID（elementId）
                    if (isNumeric(cellValue.trim())) {
                        rowData.put("elementId", cellValue.trim());
                        rowData.put("sheetIndex", sheetIndex);
                        rowData.put("sheetName", sheetName);

                        // 读取对应的规则（第9行，即索引8）
                        if (rowIndex > 7) { // 确保不是规则行本身
                            Row ruleRow = sheet.getRow(8); // 第9行的规则
                            if (ruleRow != null) {
                                Cell ruleCell = ruleRow.getCell(colIndex);
                                String ruleValue = getCellValue(ruleCell);
                                if (ruleValue != null && !ruleValue.trim().isEmpty()) {
                                    rowData.put("rule", ruleValue.trim());
                                    rowData.put("columnIndex", colIndex);
                                    rowData.put("rowIndex", rowIndex);
                                }
                            }
                        }
                        break; // 找到elementId后跳出列循环
                    }
                }
            }

            if (!rowData.isEmpty()) {
                sheetResultList.add(rowData);
            }
        }

        System.out.println("完成处理sheet[" + sheetIndex + "]: " + sheetName + " (找到 " + sheetResultList.size() + " 个elementId)");
        return sheetResultList;
    }
    // 读取 InputStream 到字节数组
    public static byte[] readInputStreamToByteArray(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        return outputStream.toByteArray();
    }

    // 读取合并单元格的值
    public static Map<String, String> readMergedCells(Sheet sheet) {
        Map<String, String> mergedCellValues = new HashMap<>();
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            int firstRow = mergedRegion.getFirstRow();
            int lastRow = mergedRegion.getLastRow();
            int firstCol = mergedRegion.getFirstColumn();
            int lastCol = mergedRegion.getLastColumn();

            String mergedCellValue = getCellValue(sheet.getRow(firstRow).getCell(firstCol));
            for (int rowNum = firstRow; rowNum <= lastRow; rowNum++) {
                for (int colNum = firstCol; colNum <= lastCol; colNum++) {
                    mergedCellValues.put(rowNum + "_" + colNum, mergedCellValue);
                }
            }
        }
        return mergedCellValues;
    }

    // 获取单元格的值
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {  // 修改方法名
            case STRING:        // 使用 CellType.STRING 枚举值
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }

    /**
     * 解析表头模板Excel文件，只解析3-5行为表头，处理合并单元格
     * @param inputStream Excel文件输入流
     * @return 解析后的表头结构，格式为[{"title": "标题", "field": "字段", "children": [{...}]}]
     * @throws Exception
     */
    public static List<Map<String, Object>> parseHeaderTemplate(InputStream inputStream) throws Exception {
        // 读取 Excel 数据到字节数组
        byte[] excelData = readInputStreamToByteArray(inputStream);
        
        List<Map<String, Object>> resultList = new ArrayList<>();

        try (InputStream poiInputStream = new ByteArrayInputStream(excelData)) {
            Workbook workbook = WorkbookFactory.create(poiInputStream);
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个Sheet
            
            if (sheet == null) {
                throw new Exception("Excel文件中未找到工作表");
            }

            // 获取3-5行 (索引2-4)
            Row row3 = sheet.getRow(2); // 第3行
            Row row4 = sheet.getRow(3); // 第4行
            Row row5 = sheet.getRow(4); // 第5行
            
            if (row3 == null || row5 == null) {
                throw new Exception("Excel文件中未找到必要的表头行");
            }

            // 获取合并单元格信息
            Map<String, String> mergedCellValues = readMergedCells(sheet);
            
            // 记录已处理的列，防止重复处理
            Set<Integer> processedColumns = new HashSet<>();
            
            // 获取最后一列的索引
            int lastCellNum = row3.getLastCellNum();
            
            // 遍历列
            for (int colIndex = 0; colIndex < lastCellNum; colIndex++) {
                // 如果已经处理过，则跳过
                if (processedColumns.contains(colIndex)) {
                    continue;
                }
                
                // 获取当前列的标题
                String title = getCellValue(row3.getCell(colIndex));
                
                // 如果为空，尝试从合并单元格获取
                if (title.trim().isEmpty()) {
                    title = mergedCellValues.getOrDefault(2 + "_" + colIndex, "");
                }
                
                // 如果标题为空，则跳过
                if (title.trim().isEmpty()) {
                    continue;
                }
                
                // 处理单元格
                Map<String, Object> headerMap = processHeaderCell(sheet, colIndex, mergedCellValues, processedColumns);
                if (!headerMap.isEmpty()) {
                    resultList.add(headerMap);
                }
            }

            workbook.close();
        }

        return resultList;
    }
    
    /**
     * 处理表头单元格，包括可能的子节点
     * @param sheet Excel工作表
     * @param colIndex 列索引
     * @param mergedCellValues 合并单元格值
     * @param processedColumns 已处理的列
     * @return 处理结果，包括标题、字段和可能的子节点
     */
    private static Map<String, Object> processHeaderCell(Sheet sheet, int colIndex, Map<String, String> mergedCellValues, Set<Integer> processedColumns) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取第3行的标题
        Row row3 = sheet.getRow(2);
        String title = getCellValue(row3.getCell(colIndex));
        
        // 如果为空，尝试从合并单元格获取
        if (title.trim().isEmpty()) {
            title = mergedCellValues.getOrDefault(2 + "_" + colIndex, "");
        }
        
        result.put("title", title);
        result.put("field", title); // 使用标题作为字段名
        
        // 获取第4行和第5行，用于后续检查
        Row row4 = sheet.getRow(3);
        Row row5 = sheet.getRow(4);
        
        // 检查该列是否有子列（检查第3行是否有合并单元格扩展到其他列）
        boolean hasChildren = false;
        List<Map<String, Object>> children = new ArrayList<>();
        
        // 找出所有属于该列父标题下的子列
        String parentCellKey = 2 + "_" + colIndex; // 第3行，当前列
        String parentValue = title;
        
        // 首先检查当前列是否有自己的子标题（比如砼用量（m³））
        // 检查第4行和第5行是否有内容
        String subTitle1 = "";
        String subTitle2 = "";
        
        // 检查第4行是否有内容
        if (row4 != null) {
            Cell cell = row4.getCell(colIndex);
            if (cell != null) {
                subTitle1 = getCellValue(cell).trim();
                // 如果第4行为空，尝试从合并单元格获取
                if (subTitle1.isEmpty()) {
                    subTitle1 = mergedCellValues.getOrDefault(3 + "_" + colIndex, "").trim();
                }
            }
        }
        
        // 检查第5行是否有内容
        if (row5 != null) {
            Cell cell = row5.getCell(colIndex);
            if (cell != null) {
                subTitle2 = getCellValue(cell).trim();
                // 如果第5行为空，尝试从合并单元格获取
                if (subTitle2.isEmpty()) {
                    subTitle2 = mergedCellValues.getOrDefault(4 + "_" + colIndex, "").trim();
                }
            }
        }
        
        // 如果第4行或第5行有非空内容，且与父标题不同，则为子标题
        String childTitle = !subTitle2.isEmpty() ? subTitle2 : subTitle1;
        if (!childTitle.isEmpty() && !childTitle.equals(title)) {
            // 这列同时是父列和子列
            Map<String, Object> childMap = new HashMap<>();
            childMap.put("title", childTitle);
            childMap.put("field", childTitle);
            children.add(childMap);
            hasChildren = true;
        }
        
        // 遍历所有列，检查哪些列的第3行与当前列合并
        for (int i = colIndex + 1; i < row3.getLastCellNum(); i++) {
            // 跳过已处理的列
            if (processedColumns.contains(i)) {
                continue;
            }
            
            // 检查列 i 的第3行是否与当前列合并
            String mergedValue = mergedCellValues.get(2 + "_" + i);
            if (mergedValue != null && mergedValue.equals(parentValue)) {
                // 列 i 的第3行与当前列合并，是一个子列
                hasChildren = true;
                
                // 获取子标题 - 首先检查第5行
                String childTitle1 = "";
                String childTitle2 = "";
                
                // 检查第4行
                if (row4 != null) {
                    Cell cell = row4.getCell(i);
                    if (cell != null) {
                        childTitle1 = getCellValue(cell).trim();
                        if (childTitle1.isEmpty()) {
                            childTitle1 = mergedCellValues.getOrDefault(3 + "_" + i, "").trim();
                        }
                    }
                }
                
                // 检查第5行
                if (row5 != null) {
                    Cell cell = row5.getCell(i);
                    if (cell != null) {
                        childTitle2 = getCellValue(cell).trim();
                        if (childTitle2.isEmpty()) {
                            childTitle2 = mergedCellValues.getOrDefault(4 + "_" + i, "").trim();
                        }
                    }
                }
                
                // 优先使用第5行，其次是第4行
                String finalChildTitle = !childTitle2.isEmpty() ? childTitle2 : childTitle1;
                
                if (!finalChildTitle.isEmpty()) {
                    Map<String, Object> childMap = new HashMap<>();
                    childMap.put("title", finalChildTitle);
                    childMap.put("field", finalChildTitle);
                    children.add(childMap);
                }
                
                // 标记为已处理
                processedColumns.add(i);
            }
        }
        
        // 如果有子节点，添加到结果中
        if (hasChildren && !children.isEmpty()) {
            result.put("children", children);
        }
        
        // 标记当前列为已处理
        processedColumns.add(colIndex);
        
        return result;
    }
}