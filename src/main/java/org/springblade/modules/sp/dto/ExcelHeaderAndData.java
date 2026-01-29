package org.springblade.modules.sp.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class ExcelHeaderAndData {
    private final   Map<String, String> header;  // 存储 lv=0 的内容
    private final List<EncodingExcelDTO> data;  // 存储第四行后的实际数据

    public ExcelHeaderAndData(  Map<String, String> header, List<EncodingExcelDTO> data) {
        this.header = header;
        this.data = data;
    }


}
