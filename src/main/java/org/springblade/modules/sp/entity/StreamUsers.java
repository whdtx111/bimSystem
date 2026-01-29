package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.List;
import java.util.UUID;

@Data
@TableName("sl_stream_users")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目人员关系对象", description = "项目人员关系实体表")
public class StreamUsers extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String streamId;
    private String userId;
    private String authLevel;
    private Integer status;

    public StreamUsers(String id, String streamId, String userId,String authLevel,Integer status) {
        this.id = UUID.randomUUID().toString();
        this.streamId = streamId;
        this.userId = userId;
        this.authLevel = authLevel;
        this.status = status;
    }

    public StreamUsers() {
        this.id = UUID.randomUUID().toString();
    }
}
