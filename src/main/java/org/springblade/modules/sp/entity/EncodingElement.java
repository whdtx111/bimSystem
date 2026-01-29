package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_encoding_element")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "编码配置实体", description = "编码配置实体表")
public class EncodingElement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String streamId;
    private String branchId;
    private String elementId;
    private String encodingId;
    private String name;
    private String code;
    private String encodingStatus;
    private String modifyUser;
    private Date modifyTime;

    public EncodingElement(String streamId, String branchId, String elementId, String encodingId,String name,String code, String encodingStatus, String modifyUser, Date modifyTime) {
        this.id = UUID.randomUUID().toString();
        this.streamId = streamId;
        this.branchId = branchId;
        this.elementId = elementId;
        this.encodingId = encodingId;
        this.name = name;
        this.code = code;
        this.encodingStatus = encodingStatus;
        this.modifyTime = new Date();
        this.modifyUser = modifyUser;
    }

    public EncodingElement() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }

}
