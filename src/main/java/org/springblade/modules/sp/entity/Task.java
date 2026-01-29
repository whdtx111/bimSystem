package org.springblade.modules.sp.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;

@Data
@TableName("sp_task")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "任务对象", description = "任务实体表")
public class Task extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String streamId;
    private String commitId;
    private String type;
    private String taskStatus;
    private String detail;
    private String modifyUser;
    private String deadLine;
    private String targetId;
    private String collisionId;
    private String url;
    private String others;

    public Task() {
    }

    public Task(String id, String streamId, String commitId, String type, String taskStatus, String detail, String modifyUser, String deadLine, String targetId, String collisionId,String url, String others) {
        this.id = UUID.randomUUID().toString();
        this.streamId = streamId;
        this.commitId = commitId;
        this.type = type;
        this.taskStatus = taskStatus;
        this.detail = detail;
        this.modifyUser = modifyUser;
        this.deadLine = deadLine;
        this.targetId = targetId;
        this.collisionId = collisionId;
        this.url = url;
        this.others = others;
}
}

