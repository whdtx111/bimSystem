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
 * 白名单实体类
 * 用于存储自定义模板的表头字段白名单
 */
@Data
@TableName("sp_white_list")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "白名单实体", description = "存储自定义模板的表头字段白名单")
public class WhiteList extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "属性名称（表头字段名）")
    private String name;

    @ApiModelProperty(value = "模板ID，关联sp_new_template表")
    private String templateId;

    @ApiModelProperty(value = "文件ID，关联sp_bom_file表")
    private String fileId;

    @ApiModelProperty(value = "streamId")
    private String streamId;

    @ApiModelProperty(value = "branchId")
    private String branchId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    public WhiteList() {
        this.id = UUID.randomUUID().toString();
        this.createTime = new Date();
        this.modifyTime = new Date();
    }

    public WhiteList(String name, String templateId, String fileId, String streamId, String branchId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.templateId = templateId;
        this.fileId = fileId;
        this.streamId = streamId;
        this.branchId = branchId;
        this.createTime = new Date();
        this.modifyTime = new Date();
    }
}
