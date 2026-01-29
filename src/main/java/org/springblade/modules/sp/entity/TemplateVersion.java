package org.springblade.modules.sp.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 模板版本实体类
 */
@Data
@TableName("sp_template_version")
public class TemplateVersion {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 模板ID
     */
    @NotBlank
    private String templateId;
    
    /**
     * 新增数量
     */
    private Integer insertCount;
    
    /**
     * 删除数量
     */
    private Integer deleteCount;
    
    /**
     * 更新数量
     */
    private Integer updateCount;
    
    /**
     * 检查时间
     */
    private String checkTime;
    
    /**
     * 版本号
     */
    private String version;
    
    /**
     * 数据（JSON格式）
     */
    private List<JSONObject> data;
    
    /**
     * 创建时间
     */
    private Date createTime;

    public TemplateVersion() {
        this.id = UUID.randomUUID().toString();
        this.createTime = new Date();
    }

    public TemplateVersion(String templateId, Integer insertCount, Integer deleteCount, Integer updateCount, String checkTime, String version) {
        this.id = UUID.randomUUID().toString();
        this.templateId = templateId;
        this.insertCount = insertCount;
        this.deleteCount = deleteCount;
        this.updateCount = updateCount;
        this.checkTime = checkTime;
        this.version = version;
        this.createTime = new Date();
    }
    
}
