package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_encoding")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "属性字典对象", description = "属性字典实体表")
public class Encoding extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String version;
    private String auth;
    private String user;
    private String source;
    private String detail;
    private String isfinished;

    private String modifyUser;
    private Date modifyTime;
    private Integer status;

    public Encoding(String name, String version, String auth, String user, String source, String detail,String isfinished, String modifyUser, Date modifyTime, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.version = version;
        this.auth = auth;
        this.user = user;
        this.source = source;
        this.detail = detail;
        this.isfinished = isfinished;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.status = status;
    }

    public Encoding() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }
}
