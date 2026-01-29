package org.springblade.modules.sp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Stream颜色数据VO
 *
 * @author auto-generated
 * @since 2026-01-05
 */
@Data
@ApiModel(value = "StreamColorDataVO对象", description = "Stream颜色数据响应对象")
public class StreamColorDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "StreamID")
    private String streamId;

    @ApiModelProperty(value = "CommitID")
    private String commitId;

    @ApiModelProperty(value = "颜色数据列表")
    private List<ColorDataItem> data;

    /**
     * 颜色数据项
     */
    @Data
    @ApiModel(value = "ColorDataItemVO对象", description = "颜色数据项响应对象")
    public static class ColorDataItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "NodeID")
        private String nodeId;

        @ApiModelProperty(value = "颜色值")
        private String color;
    }
}
