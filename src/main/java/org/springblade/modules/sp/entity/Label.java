package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * 标签实体类
 */
@Data
@TableName("sp_label")
@ApiModel(value = "Label对象", description = "标签实体表")
public class Label {

    @TableField("id")
    @ApiModelProperty(value = "主键ID")
    private String id;

    @TableField("stream_id")
    @ApiModelProperty(value = "流ID")
    private String streamId;

    @TableField("branch_id")
    @ApiModelProperty(value = "分支ID")
    private String branchId;

    @TableField("commit_id")
    @ApiModelProperty(value = "提交ID")
    private String commitId;

    @TableField("type")
    @ApiModelProperty(value = "标签类型")
    private String type;

    @TableField("text")
    @ApiModelProperty(value = "文字内容")
    private String text;

    @TableField("size")
    @ApiModelProperty(value = "大小")
    private String size;

    @TableField("color")
    @ApiModelProperty(value = "颜色")
    private String color;

    @TableField("status")
    @ApiModelProperty(value = "状态")
    private Integer status;

    @TableField("img_index")
    @ApiModelProperty(value = "图片索引")
    private String imgIndex;

    @TableField("position")
    @ApiModelProperty(value = "位置")
    private String position;

    @TableField("created_time")
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @TableField("updated_time")
    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    /**
     * 无参构造函数
     */
    public Label() {
        this.id = UUID.randomUUID().toString();
        this.status = 0; // 默认值为0
        this.createdTime = new Date();
        this.updatedTime = new Date();
    }

    /**
     * 全参构造函数
     */
    public Label(String id, String streamId, String branchId, String commitId, String type,
                 String text, String size, String color, Integer status, String imgIndex, String position) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.streamId = streamId;
        this.branchId = branchId;
        this.commitId = commitId;
        this.type = type;
        this.text = text;
        this.size = size;
        this.color = color;
        this.status = status != null ? status : 0;
        this.imgIndex = imgIndex;
        this.position = position;
        this.createdTime = new Date();
        this.updatedTime = new Date();
    }
}
