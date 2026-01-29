package org.springblade.modules.sp.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;
@Data
@TableName("sp_ebs_parameters")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "EBS对象", description = "EBS实体表")
public class EbsParameters extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    @TableField("pid")
    private String pid;
    @TableField("ebs_id")
    private String ebsId;
    @TableField("ebs_code")
    private String ebsCode;
    @TableField("ebs_name")
    private String ebsName;
    @TableField("ebs_lv")
    private String ebsLv;
    @TableField("detail")
    private String detail;
    @TableField("category")
    private String[] category;
    @TableField("ebs_status")
    private Integer ebsStatus;
    @TableField("modify_user")
    private String modifyUser;
    @TableField("modify_time")
    private Date modifyTime;

    public EbsParameters(String id,String ebsId,String pid,String ebsCode,String[] category, String ebsName, String ebsLv, String detail,String modifyUser,Integer ebsStatus) {
        this.id = UUID.randomUUID().toString();
        this.ebsId = ebsId;
        this.pid = pid;
        this.ebsCode = ebsCode;
        this.category = category;
        this.ebsName = ebsName;
        this.ebsLv = ebsLv;
        this.detail = detail;
        this.modifyUser = modifyUser;
        this.modifyTime = new DateTime();
        this.ebsStatus = ebsStatus;
    }
    public EbsParameters() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new DateTime();
    }
}
