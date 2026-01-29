package org.springblade.modules.sp.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

/**
 * 碰撞检测实体类
 * 
 * @author Yi
 * @since 2024-11-12
 */
@Data
@TableName("sp_crash_detection")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CrashDetection碰撞检测", description = "CrashDetection碰撞检测实体表")
public class CrashDetection extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "流ID")
    private String streamId;

    @ApiModelProperty(value = "分支ID")
    private String branchId;

    @ApiModelProperty(value = "提交ID")
    private String commitId;

    @ApiModelProperty(value = "MinIO文件URL")
    private String minIoUrl;

    @ApiModelProperty(value = "状态：0-成功处理，1-待处理")
    private Integer status;

    @ApiModelProperty(value = "碰撞检测阈值")
    private String threshold;

    @ApiModelProperty(value = "检测结果数据（JSON）")
    private JSONObject data;

    @ApiModelProperty(value = "修改用户")
    private String modifyUser;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    public CrashDetection() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }

    public CrashDetection(String streamId, String branchId, String commitId, String minIoUrl, 
                         Integer status, JSONObject data, String modifyUser) {
        this.id = UUID.randomUUID().toString();
        this.streamId = streamId;
        this.branchId = branchId;
        this.commitId = commitId;
        this.minIoUrl = minIoUrl;
        this.status = status;
        this.data = data;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
    }
}
