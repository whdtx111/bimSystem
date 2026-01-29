package org.springblade.modules.sp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * ObjectDTO 数据传输层
 * wangc 2023年05月10日09:14:54
 */
@Data
@ApiModel(value = "ObjectDTO对象", description = "ObjectDTO对象")
public class ObjectDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * streamsId
     */
    @ApiModelProperty(value = "streamsId")
    private String streamsId;

    /**
     * elementId
     */
    @ApiModelProperty(value = "elementId")
    private String elementId;

    /**
     * objectsId
     */
    @ApiModelProperty(value = "objectsId")
    private String objectsId;

    /**
     * 构架的data 参数
     */
//    @ApiModelProperty(value = "dataJson")
//    private String dataJson;
}
