package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 材料清单转换表实体类
 */
@Data
@TableName("material_list_conversions")
public class MaterialListConversion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * 流ID
     */
    private String streamId;
    
    /**
     * 分支ID
     */
    private String branchId;
    
    /**
     * 模板ID
     */
    private String templateId;
    
    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * MinIO URL
     */
    private String minioUrl;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 创建时间
     */
    private Date createdAt;
    
    /**
     * 更新时间
     */
    private Date updatedAt;
}
