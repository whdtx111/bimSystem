package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("lib_dic_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "内层字典对象", description = "内层字典实体表")
public class LibDicDetail extends BaseEntity {

    private String id;

    private String pid;

    private String name;

    private String value;

    private String remark;

    private Date createTime;

    private Date modifyTime;

    private String userName;

    private Integer status;

    public LibDicDetail() {
        this.id = UUID.randomUUID().toString();
        this.createTime = new Date();
    }

    public LibDicDetail(String id, String pid, String name, String value, String remark,Date createTime,Date modifyTime,String userName,Integer status) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.name = name;
        this.value = value;
        this.remark = remark;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.userName = userName;
        this.status = status;
    }
}
