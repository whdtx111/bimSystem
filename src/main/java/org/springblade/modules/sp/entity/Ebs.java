package org.springblade.modules.sp.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_ebs_ref")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "EBS对象", description = "EBS实体表")
public class Ebs extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String ebsCode;
    private String ebsName;
    private String ebsLv;
    private String type;
    private String source;
    private String auth;
    private Integer ebsStatus;

    private String modifyUser;
    private Date modifyTime;

    public Ebs(String id,String ebsCode, String ebsName,String ebsLv, String type, String source, String auth, Integer ebsStatus, String modifyUser) {
        this.id = UUID.randomUUID().toString();
        this.ebsCode = ebsCode;
        this.ebsName = ebsName;
        this.ebsLv = "0";
        this.type = type;
        this.source = source;
        this.auth = auth;
        this.ebsStatus = ebsStatus;
        this.modifyUser = modifyUser;
        this.modifyTime = new DateTime();
    }

    public Ebs() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new DateTime();
    }
}
