package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;
@Data
@TableName("sp_encoding_library")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "语义字典实体", description = "语义字典实体表")
public class EncodingLibrary extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String code;

    private String detail;

    private String lv;


    private String modifyUser;
    private Date modifyTime;
    private Integer status;


    public EncodingLibrary(String name, String code, String detail, String lv, String modifyUser, Date modifyTime, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.code = code;
        this.detail = detail;
        this.lv = lv;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.status = status;
    }

    public EncodingLibrary() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }
}
