package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("lib_dictionary")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "外层字典对象", description = "外层字典实体表")
public class LibDictionary extends BaseEntity {

    private String id;
    private String name;
    private String code;
    private Date createTime;

    private Date modifyTime;

    private String userName;
    private String remark;
    private Integer status;

    public LibDictionary(String id, String name, String code, Date createTime, Date modifyTime, String userName, String remark, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.code = code;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.userName = userName;
        this.remark = remark;
        this.status = status;
    }

    public LibDictionary() {
        this.id = UUID.randomUUID().toString();
        this.createTime = new Date();
    }

}
