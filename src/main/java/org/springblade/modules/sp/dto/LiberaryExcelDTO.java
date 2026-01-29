package org.springblade.modules.sp.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class LiberaryExcelDTO {

    @ExcelProperty("属性名称")
    private String name;

    @ExcelProperty("单位")
    private String units;

    @ExcelProperty("类别映射")
    private String category;

    @ExcelProperty("参数类型")
    private String parameterType;

    @ExcelProperty("数据类型")
    private String dataType;

    @ExcelProperty("业务参数组")
    private String parameters;

    @ExcelProperty("描述")
    private String detail;

    @ExcelProperty("专业分类")
    private String type;

    @ExcelProperty("LOD")
    private String lod;

    @ExcelProperty("Revit参数组")
    private String revitParameters;

}
