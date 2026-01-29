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
@TableName("sp_template_update")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "模板修改实体", description = "模板修改表")
public class TemplateUpdate extends BaseEntity {

    private String id;
    private String templateId;
    private String streamId;
    private String branchId;
    private List<JSONObject> data;
    private String modifyUser;
    private Date modifyTime;

    public TemplateUpdate() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }

    public TemplateUpdate(String templateId, String streamId, String branchId, List<JSONObject> data, String modifyUser) {
        this.id = UUID.randomUUID().toString();
        this.templateId = templateId;
        this.streamId = streamId;
        this.branchId = branchId;
        this.data = data;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
    }


}
