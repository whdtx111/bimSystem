package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springblade.modules.sp.entity.BranchCommits;
public interface BranchMapper extends BaseMapper<BranchCommits> {

    @Select("SELECT y.\"commitId\"" +
            "FROM branch_commits y JOIN commits c ON y.\"commitId\" = c.id " +
            "WHERE y.\"branchId\" = #{branchId} " +
            "ORDER BY c.\"createdAt\" DESC LIMIT 1")
    String getLatestCommitsIdByBranchId(@Param("branchId") String branchId);

}
