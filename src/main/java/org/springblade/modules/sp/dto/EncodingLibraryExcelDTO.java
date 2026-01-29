package org.springblade.modules.sp.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class EncodingLibraryExcelDTO {

    @ExcelProperty("项目名称")
    private String name;
    @ExcelProperty("编码")
    private String code;
    @ExcelProperty("说明")
    private String detail;
    @ExcelProperty("层级")
    private String lv;
}
