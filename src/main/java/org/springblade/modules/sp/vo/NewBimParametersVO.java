package org.springblade.modules.sp.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "NewBimParametersVO", description = "NewBimParametersVO")
public class NewBimParametersVO extends NewBimListTreeVO{
    private static final long serialVersionUID = 1L;

    private JSONObject parameters;
}
