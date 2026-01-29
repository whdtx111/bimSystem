package org.springblade.modules.sp.entity;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("exp_instances")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExpInstances文件对象", description = "ExpInstances文件对象")
/**
 * ExpInstances实体类
 *
 * @author dengtx
 * @since 2024-05-14
 */
public class ExpInstances extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String objId;
    private String streamId;
    private String commitId;
    private String branchId;
    private String type;
    private JSONObject level;
    private String units;
    private String slope;
    private String family;
    private String category;
    private String elementId;
    private JSONObject parameters;
    private String phaseCreated;
    private String smartLinkType;
    private String applicationId;
    private String slopeDirection;
    private String builtInCategory;
    private String isRevitLinkedModel;
    private JSONObject materialQuantities;
    private Date createAt;

    public ExpInstances(String id,String objId, String streamId,String commitId, String branchId,String type, JSONObject level, String units, String slope, String family, String category, String elementId, JSONObject parameters, String phaseCreated, String smartLinkType, String applicationId, String slopeDirection, String builtInCategory, String isRevitLinkedModel, JSONObject materialQuantities) {
        this.id = UUID.randomUUID().toString();
        this.objId = objId;
        this.streamId = streamId;
        this.commitId = commitId;
        this.branchId = branchId;
        this.type = type;
        this.level = level;
        this.units = units;
        this.slope = slope;
        this.family = family;
        this.category = category;
        this.elementId = elementId;
        this.parameters = parameters;
        this.phaseCreated = phaseCreated;
        this.smartLinkType = smartLinkType;
        this.applicationId = applicationId;
        this.slopeDirection = slopeDirection;
        this.builtInCategory = builtInCategory;
        this.isRevitLinkedModel = isRevitLinkedModel;
        this.materialQuantities = materialQuantities;
        this.createAt = new DateTime();
    }
}
