package org.springblade.modules.sp.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("exp_assembly")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExpAssembly对象", description = "ExpAssembly对象")
public class ExpAssembly extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String streamId;
    private String commitId;
    private JSONArray assembly;  // 改为 JSONArray
    private Date createdAt;

    public ExpAssembly() {
        this.id = UUID.randomUUID().toString();
    }

    public ExpAssembly(String streamId, String commitId, JSONArray assembly) {  // 改为 JSONArray
        this.id = UUID.randomUUID().toString();
        this.streamId = streamId;
        this.commitId = commitId;
        this.assembly = assembly;
        this.createdAt = new Date();
    }
}
