package org.springblade.modules.sp.dto;

import lombok.Data;
import org.springblade.modules.sp.entity.ElementParameter;

import java.util.List;
@Data
public class ElementParameterDTO {

    private String wbsCode;
    private String tempId;
    private List<ElementParameter> elementParameters;

}
