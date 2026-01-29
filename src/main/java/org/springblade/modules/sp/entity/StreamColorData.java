package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

/**
 * Stream颜色数据实体类
 *
 * @author auto-generated
 * @since 2026-01-05
 */
@Data
@TableName("sp_stream_color_data")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StreamColorData对象", description = "Stream颜色数据对象")
public class StreamColorData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "StreamID")
    private String streamId;

    @ApiModelProperty(value = "CommitID")
    private String commitId;

    @ApiModelProperty(value = "NodeID")
    private String nodeId;

    @ApiModelProperty(value = "颜色值")
    private String color;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    @ApiModelProperty(value = "状态")
    private Integer status;

    public StreamColorData() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
        this.status = 1;
    }

    public StreamColorData(String streamId, String commitId, String nodeId, String color) {
        this.id = UUID.randomUUID().toString();
        this.streamId = streamId;
        this.commitId = commitId;
        this.nodeId = nodeId;
        this.color = color;
        this.modifyTime = new Date();
        this.status = 1;
    }
}
