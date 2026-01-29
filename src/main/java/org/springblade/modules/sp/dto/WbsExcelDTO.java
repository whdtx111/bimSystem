package org.springblade.modules.sp.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class WbsExcelDTO {

    @ExcelProperty("WBS分解编码")
    private String wbsCode;

    @ExcelProperty("层级")
    private String wbsLv;

    @ExcelProperty("名称")
    private String wbsName;

    @ExcelProperty("备注")
    private String detail;

}
