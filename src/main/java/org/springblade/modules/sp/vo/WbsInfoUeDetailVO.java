package org.springblade.modules.sp.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "WbsInfoUeDetailsVO对象", description = "WbsInfoUeDetailsVO对象")
public class WbsInfoUeDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ObjId;
    private String objStreamId;
    private String objType;
    private String objUnits;
    private String objFamily;
    private String objCategory;
    private String objElementId;
}
