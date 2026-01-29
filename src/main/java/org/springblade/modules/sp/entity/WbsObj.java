package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_wbs_obj")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WBSOBJ对象", description = "WBSOBJ实体表")
public class WbsObj extends BaseEntity {

    private String id;
    private String wbsId;
    private String objectId;

    private Integer status;
    private String modifyUser;
    private Date modifyTime;

    public WbsObj(String id, String wbsId, String objectId, Integer status, String modifyUser, Date modifyTime) {
        this.id = UUID.randomUUID().toString();
        this.wbsId = wbsId;
        this.objectId = objectId;
        this.status = status;
        this.modifyUser = modifyUser;
        this.modifyTime = modifyTime;
    }

    public WbsObj() {
    }
}
