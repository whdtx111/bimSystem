package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_new_template")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "新数据模板实体", description = "新数据模板实体")
public class NewTemplate extends BaseEntity {

    private String id;
    private String pid;
    private String fileId;
    private String templateId;
    private String streamListId;
    private String streamId;
    private String branchId;
    private String blockId;
    private String name;
    private String type;
    private String description;
    private String auth;
    private String source;
    private String version;
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @com.fasterxml.jackson.annotation.JsonRawValue
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.StringSerializer.class)
    private String detail;
    private String modifyUser;
    private Date modifyTime;
    private Integer status;

    public NewTemplate() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }

    public NewTemplate(String id, String pid,String fileId,String templateId,String streamListId,String streamId,String branchId,String name, String type, String description,String auth,String source,String version, String detail, String modifyUser, Date modifyTime, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.fileId = fileId;
        this.templateId = templateId;
        this.streamListId = streamListId;
        this.streamId = streamId;
        this.branchId = branchId;
        this.name = name;
        this.type = type;
        this.description = detail;
        this.auth = auth;
        this.source = source;
        this.version = version;
        this.detail = detail;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.status = status;
    }
}
