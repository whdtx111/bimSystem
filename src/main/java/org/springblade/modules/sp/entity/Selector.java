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

@Data
@TableName("sp_selector")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Selector选择集实体对象", description = "Selector选择集回传表")
public class Selector extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    @TableField("commit_ids")
    private String[] commitIds;
    @TableField("stream_id")
    private String streamId;
    @TableField("element_ids")
    private String[] elementIds;
    @TableField("select_ids")
    private String[] selectIds;
    @TableField("name")
    private String name;
    @TableField("color")
    private String color;
    @TableField("conditions")
    private List<JSONObject> conditions;
    @TableField(value = "modify_user")
    private String modifyUser;
    @TableField("modify_time")
    private Date modifyTime;
    @TableField("status")
    private Integer status;


    public Selector(String[] commitIds, String streamId,String[] selectIds ,String[] elementIds, String name, String color, List<JSONObject> conditions, String modifyUser) {
        this.id = UUID.randomUUID().toString();
        this.commitIds = commitIds;
        this.streamId = streamId;
        this.selectIds = selectIds;
        this.elementIds = elementIds;
        this.name = name;
        this.color = color;
        this.conditions = conditions;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.status = 0;
    }

    public Selector() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
        this.status = 0;
    }
}
