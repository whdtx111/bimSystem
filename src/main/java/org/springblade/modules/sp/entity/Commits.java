package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

@Data
@TableName("branch_commits")
@ApiModel(value = "BranchCommits对象", description = "BranchCommits对象")
public class Commits {

    @TableField("id")
    private String id;
    @TableField("referencedObject")
    private String referencedObject;
    @TableField("author")
    private String author;
    @TableField("message")
    private String message;
    @TableField("created_at")
    private Date createdAt;
    @TableField("sourceApplication")
    private String sourceApplication;
    @TableField(value = "sourceApplication",jdbcType = JdbcType.INTEGER)
    private Integer totalChildrenCount;
    @TableField(value = "parents",jdbcType = JdbcType.ARRAY)
    private String parents;
}
