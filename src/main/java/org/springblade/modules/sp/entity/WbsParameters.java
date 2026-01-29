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
@TableName("sp_wbs_parameters")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WBS对象", description = "WBS实体表")
public class WbsParameters extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    @TableField("pid")
    private String pid;
    @TableField("wbs_id")
    private String wbsId;
    @TableField("wbs_code")
    private String wbsCode;
    @TableField("wbs_name")
    private String wbsName;
    @TableField("wbs_lv")
    private String wbsLv;
    @TableField("detail")
    private String detail;
    @TableField("category")
    private String[]  category;
    @TableField("wbs_status")
    private Integer wbsStatus;
    @TableField("modify_user")
    private String modifyUser;
    @TableField("modify_time")
    private Date modifyTime;

    public WbsParameters(String id,String wbsId,String pid,String wbsCode,String[] category, String wbsName, String wbsLv, String detail,String modifyUser,Integer wbsStatus) {
        this.id = UUID.randomUUID().toString();
        this.wbsId = wbsId;
        this.pid = pid;
        this.wbsCode = wbsCode;
        this.category = category;
        this.wbsName = wbsName;
        this.wbsLv = wbsLv;
        this.detail = detail;
        this.modifyUser = modifyUser;
        this.modifyTime = new DateTime();
        this.wbsStatus = wbsStatus;
    }
    public WbsParameters() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new DateTime();
    }
}
