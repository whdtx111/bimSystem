package org.springblade.modules.sp.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class EncodingExcelDTO {

    @ExcelProperty("编码")
    private String code;
    @ExcelProperty("名称")
    private String name;
    @ExcelProperty("层级")
    private String lv;
    @ExcelProperty("REVIT CATEGORY")
    private String[] category;

}
