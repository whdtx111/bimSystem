package org.springblade.modules.sp.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("xia_san_shot")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "XiaSanShot对象", description = "XiaSanShot对象")
public class XiaSanShot extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String streamId;
    private String commitId;
    private String objectId;
    private String elementId;
    private String checkStatus;
    private JSONObject data;

    private Date modifyTime;
    private String modifyUser;

    public XiaSanShot(String streamId, String commitId, String objectId, String elementId,String checkStatus, String modifyUser, Date modifyTime, JSONObject data) {
        this.id = UUID.randomUUID().toString();
        this.streamId = streamId;
        this.commitId = commitId;
        this.objectId = objectId;
        this.elementId = elementId;
        this.checkStatus = checkStatus;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.data = data;
    }


    public XiaSanShot() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }
}
