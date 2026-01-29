package org.springblade.modules.sp.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@TableName("sp_encoding_config")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "编码配置实体", description = "编码配置实体表")
public class EncodingConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    private String encodingParametersId;

    private String lv;

    private String lvCode;

    private List<JSONObject> config;

    private String modifyUser;
    private Date modifyTime;
    private Integer status;

    public EncodingConfig(String encodingParametersId, String lv, String lvCode, List<JSONObject> config, String modifyUser,Date modifyTime, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.encodingParametersId = encodingParametersId;
        this.lv = lv;
        this.lvCode = lvCode;
        this.config = config;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.status = status;
    }

    public EncodingConfig() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }

}
