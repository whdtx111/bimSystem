package org.springblade.modules.sp.vo;

import lombok.Data;
import org.springblade.modules.sp.entity.WbsParameters;

import java.util.List;

@Data
public class WbsParametersVO {

    private List<WbsParameters> wbsParametersList;
    private String type;

    public WbsParametersVO(List<WbsParameters> wbsParametersList, String type) {
        this.wbsParametersList = wbsParametersList;
        this.type = type;
    }

    public WbsParametersVO() {
    }
}
