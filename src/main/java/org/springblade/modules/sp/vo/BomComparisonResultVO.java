package org.springblade.modules.sp.vo;

import lombok.Data;

@Data
public class BomComparisonResultVO {
    private String elementId;
    private String rule;
    private String category;
    private String type;
    private String expectedCategory;
    private String expectedType;
    private Boolean matched;
    private String message;
    private Integer columnIndex;
    private Integer rowIndex;
}
