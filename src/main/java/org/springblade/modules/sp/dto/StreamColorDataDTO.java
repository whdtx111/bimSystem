package org.springblade.modules.sp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * Stream颜色数据DTO
 *
 * @author auto-generated
 * @since 2026-01-05
 */
@Data
@ApiModel(value = "StreamColorDataDTO对象", description = "Stream颜色数据请求对象")
public class StreamColorDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "streamId不能为空")
    @ApiModelProperty(value = "StreamID", required = true)
    private String streamId;

    @NotBlank(message = "commitId不能为空")
    @ApiModelProperty(value = "CommitID", required = true)
    private String commitId;

    @NotEmpty(message = "data不能为空")
    @Valid
    @ApiModelProperty(value = "颜色数据列表", required = true)
    private List<ColorDataItem> data;

    /**
     * 颜色数据项
     */
    @Data
    @ApiModel(value = "ColorDataItem对象", description = "颜色数据项")
    public static class ColorDataItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @NotBlank(message = "nodeId不能为空")
        @ApiModelProperty(value = "NodeID", required = true)
        private String nodeId;

        @ApiModelProperty(value = "颜色值")
        private String color;
    }
}
