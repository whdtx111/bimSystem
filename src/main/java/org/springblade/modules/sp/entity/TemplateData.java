package org.springblade.modules.sp.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@TableName("sp_template_data")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "模板数据", description = "模板数据实体表")
public class TemplateData extends BaseEntity {

    private String id;
    private String templateId;
    private String type;
    private String branchId;
    private List<JSONObject> data;
    private Date createdTime;


    public TemplateData() {
        this.id = UUID.randomUUID().toString();
        this.createdTime = new Date();
    }

    private TemplateData(String id, String templateId, String type, List<JSONObject> data, Date createdTime) {
        this.id = UUID.randomUUID().toString();
        this.templateId = templateId;
        this.type = type;
        this.data = data;
        this.createdTime = new Date();
    }
}
