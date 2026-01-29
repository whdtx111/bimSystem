package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;

@Data
@TableName("sp_wbs_lv")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WbsLv对象", description = "WbsLv实体表")
public class WbsLv extends BaseEntity {

    private static final long serialVersionUID = 1L;


    private String id;
    private String code;
    private String nameCn;
    private String wbsId;


    public WbsLv(String id, String code, String nameCn, String wbsId) {
        this.id = id;
        this.code = code;
        this.nameCn = nameCn;
        this.wbsId = wbsId;
    }

    public WbsLv() {
        this.id = UUID.randomUUID().toString();
    }
}
