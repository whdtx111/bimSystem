package org.springblade.modules.sp.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;
@Data
@TableName("sp_wbs_ref")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WBS对象", description = "WBS实体表")
public class Wbs extends BaseEntity {

    private static final long serialVersionUID = 1L;


    private String id;
    private String wbsCode;
    private String wbsName;
    private String wbsLv;
    private String type;
    private String source;
    private String auth;
    private Integer wbsStatus;

    private String modifyUser;
    private Date modifyTime;


    public Wbs(String id,String wbsCode, String wbsName,String wbsLv, String type, String source, String auth, Integer wbsStatus, String modifyUser) {
        this.id = UUID.randomUUID().toString();
        this.wbsCode = wbsCode;
        this.wbsName = wbsName;
        this.wbsLv = "0";
        this.type = type;
        this.source = source;
        this.auth = auth;
        this.wbsStatus = wbsStatus;
        this.modifyUser = modifyUser;
        this.modifyTime = new DateTime();
    }

    public Wbs() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new DateTime();
    }
}
