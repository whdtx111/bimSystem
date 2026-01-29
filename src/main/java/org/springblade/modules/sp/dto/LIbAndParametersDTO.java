package org.springblade.modules.sp.dto;

import lombok.Data;
import org.springblade.modules.sp.entity.ElementParameter;
import org.springblade.modules.sp.entity.Library;

import java.util.List;

@Data
public class LIbAndParametersDTO {
    private List<Library> librarys;
    private List<ElementParameter> parameters;
}
