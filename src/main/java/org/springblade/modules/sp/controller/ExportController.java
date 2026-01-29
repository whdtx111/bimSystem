package org.springblade.modules.sp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.dto.FinalExcel01;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.service.*;
import org.springblade.modules.sp.vo.Page03VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/excel")
@Api(value = "表格导出", tags = "表格导出")
public class ExportController extends BladeController {

    @Autowired
    private TemplateExportService templateExportService;
    @Autowired
    private ITemplateService templateService;
    @Autowired
    private Configuration freemarkerConfig;
    @Autowired
    private WbsLvService wbsLvService;

    @Autowired
    private WbsParametersService wbsParametersService;
    @Autowired
    private LibParametersService libParametersService;
    @Autowired
    private LibraryService libraryService;
    @Autowired
    private WbsParametersCopyService wbsParametersCopyService;
    @Autowired
    private LibWbsTempService libWbsTempService;
    @Autowired
    private  ElementWbsService elementWbsService;
    @Autowired
    private  ExpInstancesService expInstancesService;
    @Autowired
    private  ElementParameterService elementParameterService;


    @SneakyThrows
    @GetMapping("/exportWbsExcel.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "exportWbsExcel", notes = "exportWbsExcel")
    public void exportWbsExcel(
            @RequestParam String templateName,
            @RequestParam String wbsId,
            HttpServletResponse response
    ) throws Exception {
        // 根据实体类型获取数据
        Map<String, Object> dataModel = new HashMap<>();
        String outputFileName;
        List<WbsParameters> wbsList = wbsParametersService.getAllWbsParametersByWbsId(wbsId);
        dataModel.put("wbsList", wbsList);
        outputFileName = "wbs_parameters";

        // 调用通用导出服务生成文件
        File file = templateExportService.exportToFile(templateName, dataModel, outputFileName);

        // 设置响应头并输出文件
        response.setContentType("application/vnd.ms-excel");
        String fileName = URLEncoder.encode(outputFileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }

        // 删除临时文件
        file.delete();
    }

    @SneakyThrows
    @GetMapping("/exportPage3Excel.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "exportPage3Excel", notes = "exportPage3Excel")
    public void exportPage3Excel(
            @RequestParam String tempId,@RequestParam String streamId,@RequestParam String branchId,@RequestParam String commitId,
            HttpServletResponse response
    ){
        try {
            List<Page03VO> resList = new ArrayList<>();

                //wbsCode为空，根据模板获取全量
                List<String> wbsCodeByTempId = libWbsTempService.getWbsCodeByTempId(tempId);
            // 预加载所有需要的数据
            Map<String, LibWbsTemp> libWbsTempMap = new HashMap<>();
            for (String code : wbsCodeByTempId) {
                libWbsTempMap.put(code, libWbsTempService.getLibWbsTemp(code, tempId));
            }

// 获取所有 WbsParametersCopy 数据并组织为 Map
            Map<String, String> wbsCodeToNameMap = wbsParametersCopyService.getWbsParametersCopyByTempId(tempId).stream()
                    .collect(Collectors.toMap(
                            WbsParametersCopy::getWbsCode,
                            WbsParametersCopy::getWbsName,
                            (existing, replacement) -> existing // 保留第一个 WbsName
                            // 或 (existing, replacement) -> replacement 保留最后一个 WbsName
                    ));

// 预加载所有 Library 数据
            Map<String, Library> libraryMap = libWbsTempMap.values().stream()
                    .flatMap(libWbsTemp -> Arrays.stream(Optional.ofNullable(libWbsTemp.getLibIds()).orElse(new String[0])))
                    .distinct()
                    .collect(Collectors.toMap(libId -> libId, libraryService::getById));

// 预加载所有 ElementParameter 数据
            Map<String, ElementParameter> parameterMap = libWbsTempMap.values().stream()
                    .flatMap(libWbsTemp -> Arrays.stream(Optional.ofNullable(libWbsTemp.getParameterIds()).orElse(new String[0])))
                    .distinct()
                    .collect(Collectors.toMap(paramId -> paramId, elementParameterService::getById));

// 处理数据
            for (String code : wbsCodeByTempId) {
                LibWbsTemp libWbsTemp = libWbsTempMap.get(code);
                if (libWbsTemp == null) continue;

                ElementWbs elementWbs = elementWbsService.getElementWbs(code, branchId, tempId);
                if (elementWbs == null || elementWbs.getElementId() == null) continue;

                List<ExpInstances> expInstancesByElements = expInstancesService.getExpInstancesByElements(streamId, elementWbs.getElementId(), commitId);

                for (ExpInstances expInstances : expInstancesByElements) {
                    String wbsName = wbsCodeToNameMap.get(code);

                    // 添加用户属性
                    if (ObjectUtils.isNotEmpty(libWbsTemp.getLibIds())) {
                        for (String libId : libWbsTemp.getLibIds()) {
                            Library library = libraryMap.get(libId);
                            if (library != null) {
                                Page03VO res = new Page03VO();
                                res.setWbsCode(code);
                                res.setWbsName(wbsName);
                                res.setElementId(expInstances.getElementId());
                                res.setType(expInstances.getType());
                                res.setName(library.getName());
                                res.setValue("");
                                res.setUnit(library.getUnits());
                                res.setCategory(library.getCategory());
                                resList.add(res);
                            }
                        }
                    }

                    // 添加构建属性
                    if (ObjectUtils.isNotEmpty(libWbsTemp.getParameterIds())) {
                        for (String paramId : libWbsTemp.getParameterIds()) {
                            ElementParameter elementParameter = parameterMap.get(paramId);
                            if (elementParameter != null) {
                                Page03VO res = new Page03VO();
                                res.setWbsCode(code);
                                res.setWbsName(wbsName);
                                res.setElementId(expInstances.getElementId());
                                res.setType(expInstances.getType());
                                res.setName(elementParameter.getName());
                                res.setValue("");
                                res.setUnit(elementParameter.getUnits());
                                res.setCategory(elementParameter.getCategory());
                                resList.add(res);
                            }
                        }
                    }
                }
            }


            TemplateDO temp = templateService.getById(tempId);
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("resList", resList);
            String outputFileName = "page3";

            // 调用通用导出服务生成文件
            File file = templateExportService.exportToFile("page03_template.ftl", dataModel, outputFileName);

            // 设置响应头并输出文件
            response.setContentType("application/vnd.ms-excel");
            String fileName = temp.getName() + "构建信息.xls";
            fileName = URLEncoder.encode(fileName, "UTF-8"); // 对文件名进行 URL 编码，防止中文乱码
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            // 删除临时文件
            file.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @GetMapping("/exportExcelSheet.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "exportExcelSheet", notes = "exportExcelSheet")
    public void exportExcelSheet(String tempId, HttpServletResponse response) throws Exception {
        TemplateDO temp = templateService.getById(tempId);

        // Step 1: 获取所有 WbsLv 数据，根据 wbsId 过滤
        List<WbsLv> wbsLvList = wbsLvService.getWbsLvByWbsId(temp.getWbsGroup());
        if (wbsLvList == null || wbsLvList.isEmpty()) {
            throw new IllegalArgumentException("未找到对应的 WbsLv 数据");
        }

        // Step 2: 创建 Excel 工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        // Step 3: 遍历每个 WbsLv 的 nameCn，并为其创建一个 Sheet
        for (WbsLv wbsLv : wbsLvList) {
            String sheetName = wbsLv.getNameCn();
            if (sheetName.length() > 31) {
                sheetName = sheetName.substring(0, 31); // 限制 Sheet 名称长度
            }

            // 获取该 WbsLv 对应的 FinalExcel01 数据
            List<FinalExcel01> sheetData = getFinalExcelDataByWbsLv(tempId, wbsLv.getCode());
            if (sheetData.isEmpty()) {
                continue; // 跳过无数据的 Sheet
            }

            // 创建子 Sheet
            HSSFSheet sheet = workbook.createSheet(sheetName);

            // Step 4: 设置标题行
            HSSFRow titleRow = sheet.createRow(0);
            HSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("系统专业参数");
            // 合并单元格（第一行跨越 6 列）
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

            // 设置标题行样式
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            HSSFFont titleFont = workbook.createFont();
            titleFont.setFontName("Arial");
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14); // 设置字体大小
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER); // 水平居中
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
            titleCell.setCellStyle(titleStyle);

            // Step 5: 填充表头
            HSSFRow headerRow = sheet.createRow(1); // 表头行放在第 2 行
            headerRow.createCell(0).setCellValue("WBS Name");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Value");
            headerRow.createCell(3).setCellValue("Unit");
            headerRow.createCell(4).setCellValue("Data Type");
            headerRow.createCell(5).setCellValue("Detail");

            // Step 6: 填充数据
            int rowNum = 2; // 数据从第 3 行开始
            for (FinalExcel01 data : sheetData) {
                HSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(data.getWbsName());
                row.createCell(1).setCellValue(data.getName());
                row.createCell(2).setCellValue(data.getValue());
                row.createCell(3).setCellValue(data.getUnit());
                row.createCell(4).setCellValue(data.getDataType());
                row.createCell(5).setCellValue(data.getDetail());
            }
        }

        // Step 7: 设置响应头并输出文件
        String fileName = temp.getName() + "系统专业参数.xls";
        fileName = URLEncoder.encode(fileName, "UTF-8"); // 对文件名进行 URL 编码，防止中文乱码
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
        } finally {
            workbook.close();
        }
    }

    /**
     * 根据 WbsLv 的 code 获取对应的 FinalExcel01 数据
     */
    private List<FinalExcel01> getFinalExcelDataByWbsLv(String tempId, String wbsLvCode) {
        List<FinalExcel01> result = new ArrayList<>();
        List<LibParameters> libParameters = libParametersService.filterLibParameters("", tempId, "");
        for (LibParameters libParameter : libParameters) {
            WbsParametersCopy wbsParameters = wbsParametersCopyService.getWbsParametersCopyByWbsCodeTempId(libParameter.getWbsCode(), tempId);
            if (wbsLvCode.equals(wbsParameters.getWbsLv())) {
                FinalExcel01 finalExcel01 = new FinalExcel01();
                Library lib = libraryService.getById(libParameter.getLibId());
                finalExcel01.setWbsName(wbsParameters.getWbsName());
                finalExcel01.setName(libParameter.getName());
                finalExcel01.setValue(libParameter.getValue());
                finalExcel01.setUnit(libParameter.getUnit());
                finalExcel01.setDataType(lib.getDataType());
                finalExcel01.setDetail(lib.getDetail());
                result.add(finalExcel01);
            }
        }
        return result;
    }
}


