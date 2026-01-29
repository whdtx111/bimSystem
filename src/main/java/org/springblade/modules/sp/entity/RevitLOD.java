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
@TableName("sp_revit_lod")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RevitLOD检测数据", description = "RevitLOD检测数据实体表")
public class RevitLOD extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String checkId;
    private String streamId;
    private String branchId;
    private String commitId;
    private String name;
    private Integer valuePass;
    private Integer valueNotPass;
    private String checkStatus;
    private String isPass;
    private List<JSONObject> data;
    private Date createdTime;

    public RevitLOD() {
        this.id = UUID.randomUUID().toString();
        this.createdTime = new Date();
    }

    public RevitLOD(String checkId, String streamId, String branchId, String commitId, String name, 
                   Integer valuePass, Integer valueNotPass, String checkStatus, String isPass, 
                   List<JSONObject> data) {
        this.id = UUID.randomUUID().toString();
        this.checkId = checkId;
        this.streamId = streamId;
        this.branchId = branchId;
        this.commitId = commitId;
        this.name = name;
        this.valuePass = valuePass;
        this.valueNotPass = valueNotPass;
        this.checkStatus = checkStatus;
        this.isPass = isPass;
        this.data = data;
        this.createdTime = new Date();
    }

}
