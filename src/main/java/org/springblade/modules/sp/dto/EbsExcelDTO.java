package org.springblade.modules.sp.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class EbsExcelDTO {

    @ExcelProperty("EBS分解编码")
    private String ebsCode;

    @ExcelProperty("层级")
    private String ebsLv;

    @ExcelProperty("名称")
    private String ebsName;

    @ExcelProperty("备注")
    private String detail;
}
