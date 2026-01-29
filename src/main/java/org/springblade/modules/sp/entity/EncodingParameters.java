package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_encoding_parameters")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "EncodingParameters对象", description = "EncodingParameters对象")
public class EncodingParameters extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    @TableField("pid")
    private String pid;
    @TableField("encoding_id")
    private String encodingId;
    @TableField("code")
    private String code;
    @TableField("name")
    private String name;
    @TableField("lv")
    private String lv;
    @TableField("category")
    private String[]  category;
    @TableField("encoding_status")
    private String encodingStatus;
    @TableField("version")
    private String version;

    @TableField("modify_user")
    private String modifyUser;
    @TableField("modify_time")
    private Date modifyTime;


    public EncodingParameters(String pid, String encodingId, String code, String name, String lv,String[] category,String encodingStatus,String version, String modifyUser, Date modifyTime) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.encodingId = encodingId;
        this.code = code;
        this.name = name;
        this.lv = lv;
        this.category = category;
        this.encodingStatus = encodingStatus;
        this.version = version;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
    }

    public EncodingParameters() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }

}
