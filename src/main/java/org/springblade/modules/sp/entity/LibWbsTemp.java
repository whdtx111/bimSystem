package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;

@Data
@TableName("sp_lib_wbs_temp")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LibWbsTemp", description = "LibWbsTemp")
public class LibWbsTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;
    @TableField("wbs_code")
    private String wbsCode;
    @TableField("temp_id")
    private String tempId;
    @TableField("lib_ids")
    private String[] libIds;
    @TableField("parameter_ids")
    private String[] parameterIds;
    @TableField(value = "status",jdbcType = JdbcType.INTEGER)
    private Integer status;

    public LibWbsTemp(String wbsCode, String tempId, String[] libIds, String[] parameterIds, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.wbsCode = wbsCode;
        this.tempId = tempId;
        this.libIds = libIds;
        this.parameterIds = parameterIds;
        this.status = status;
    }

    public LibWbsTemp() {
    }
}
