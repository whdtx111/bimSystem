package org.springblade.modules.sp.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 自定义模板属性回传实体类
 */
@Data
@TableName("sp_template_custom_data")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "自定义模板属性回传实体对象", description = "自定义模板属性回传表")
public class TemplateCustomData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    
    @TableField("obj_id")
    private String objId;
    
    @TableField("template_id")
    private String templateId;
    
    @TableField("element_id")
    private String elementId;
    
    @TableField("stream_id")
    private String streamId;
    
    @TableField("branch_id")
    private String branchId;
    
    @TableField("commit_id")
    private String commitId;
    
    @TableField("category")
    private String category;
    
    @TableField("type")
    private String type;
    
    @TableField("detail")
    private List<JSONObject> detail;
    
    @TableField("isfinished")
    private Integer isfinished;

    @TableField(value = "modify_user")
    private String modifyUser;
    
    @TableField("modify_time")
    private Date modifyTime;
    
    @TableField("status")
    private Integer status;

    public TemplateCustomData(String objId, String templateId, String elementId, String streamId, 
                              String branchId, String commitId, String category, String type, 
                              List<JSONObject> detail, Integer isfinished, String modifyUser) {
        this.id = UUID.randomUUID().toString();
        this.objId = objId;
        this.templateId = templateId;
        this.elementId = elementId;
        this.streamId = streamId;
        this.branchId = branchId;
        this.commitId = commitId;
        this.category = category;
        this.type = type;
        this.detail = detail;
        this.isfinished = isfinished;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.status = 1;
    }

    public TemplateCustomData() {
    }
}
