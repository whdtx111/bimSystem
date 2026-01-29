package org.springblade.modules.sp.entity;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_cdex_element")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CdexElement对象", description = "CdexElement实体表")
public class CdexElement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String data;

    private Date modifyTime;

    public CdexElement(String id, String name, String data, Date modifyTime) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.data = data;
        this.modifyTime = new DateTime();
    }

    public CdexElement() {}
}
