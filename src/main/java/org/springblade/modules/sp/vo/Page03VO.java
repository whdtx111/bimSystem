package org.springblade.modules.sp.vo;

import lombok.Data;

@Data
public class Page03VO {

    private String wbsName;
    private String wbsCode;
    private String elementId;
    private String type;
    private String name;
    private String value;
    private String unit;
    private String category;

    public Page03VO() {
    }
    public Page03VO(String wbsName, String wbsCode, String elementId, String type, String name, String value, String unit, String category) {
        this.wbsName = wbsName;
        this.wbsCode = wbsCode;
        this.elementId = elementId;
        this.type = type;
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.category = category;
    }
}
