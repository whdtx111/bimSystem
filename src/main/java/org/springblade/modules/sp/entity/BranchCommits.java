package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("branch_commits")
@ApiModel(value = "BranchCommits对象", description = "BranchCommits对象")
public class BranchCommits {

    @TableField("branchId")
    private String branchId;

    @TableField("commitId")
    private String commitId;

    @TableField(exist = false) // 该字段不对应数据库表中的列
    private Commits commits;
}
