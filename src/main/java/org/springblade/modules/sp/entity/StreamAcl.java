package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

@Data
@TableName("stream_acl")
@ApiModel(value = "StreamAcl对象", description = "StreamAcl对象")
public class StreamAcl extends BaseEntity {

    private String userId;
    private String resourceId;
    private String role;

    public StreamAcl() {}

    public StreamAcl(String userId, String resourceId, String role) {
        this.userId = userId;
        this.resourceId = resourceId;
        this.role = role;
    }
}
