package org.springblade.modules.sp.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

/**
 * @author huang can/dengtx
 * @since 2024/3/19 11:09
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sp_template_list")
public class TemplateDO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private String id;
    @TableField(value = "pid", jdbcType = JdbcType.VARCHAR)
    private String pid;
    @TableField(value = "name", jdbcType = JdbcType.VARCHAR)
    private String name;
    @TableField(value = "wbs_group", jdbcType = JdbcType.VARCHAR)
    private String wbsGroup;
    @TableField(value = "type", jdbcType = JdbcType.VARCHAR)
    private String type;
    @TableField(value = "branch_id", jdbcType = JdbcType.VARCHAR)
    private String branchId;
    @TableField(value = "stream_id", jdbcType = JdbcType.VARCHAR)
    private String streamId;
    @TableField(value = "detail", jdbcType = JdbcType.VARCHAR)
    private String detail;
    @TableField(value = "ranknum", jdbcType = JdbcType.FLOAT)
    private Float ranknum;
    @TableField(value = "status", jdbcType = JdbcType.INTEGER)
    private Integer status;
    @TableField(value = "modify_user", jdbcType = JdbcType.VARCHAR)
    private String modifyUser;
    @TableField(value = "modify_time", jdbcType = JdbcType.TIMESTAMP)
    private Date modifyTime;

    public TemplateDO(String pid, String name,String wbsGroup ,String type, String branchId,String streamId,String detail,String modifyUser,Float ranknum) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.name = name;
        this.wbsGroup = wbsGroup;
        this.type = type;
        this.branchId = branchId;
        this.streamId = streamId;
        this.detail = detail;
        this.status = 0;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.ranknum = ranknum;
    }

    public TemplateDO() {
    }
}
