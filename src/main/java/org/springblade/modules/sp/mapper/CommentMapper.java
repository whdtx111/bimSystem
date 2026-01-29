package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springblade.modules.sp.entity.Comment;

import java.util.List;
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    // 根据id查询单个Comment
    @Select("SELECT * FROM sp_comment WHERE id = #{id}")
    Comment getCommentById(@Param("id") String id);

    // 根据ids查询多个Comment
    @Select("SELECT * FROM sp_comment WHERE id = ANY(#{id}::text[])")
    List<Comment> getCommentsByIds(@Param("id") String[] id);

    // 查询所有的Comment
    @Select("SELECT * FROM sp_comment")
    List<Comment> getAllComments();

    // 插入一个新的Comment
    @Insert("INSERT INTO sp_comment (id, url) VALUES (#{id}, #{url})")
    int insertComment(Comment comment);

    // 更新一个Comment
    @Update("UPDATE sp_comment SET url = #{url} WHERE id = #{id}")
    int updateComment(Comment comment);

    // 删除一个Comment
    @Delete("DELETE FROM sp_comment WHERE id = #{id}")
    int deleteComment(@Param("id") String id);

    // 使用MyBatis-Plus的Wrapper更新字段
    @Update("UPDATE sp_comment SET url = #{url} WHERE id = #{id}")
    int updateCommentWithWrapper(@Param("id") String id, @Param("url") String url);

    // 根据url查找评论
    @Select("SELECT * FROM sp_comment WHERE url = #{url}")
    List<Comment> getCommentsByUrl(@Param("url") String url);

    // 根据某些条件查询（使用QueryWrapper）
    default List<Comment> getCommentsByCondition(String url) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (url != null) {
            queryWrapper.eq("url", url);
        }
        return selectList(queryWrapper);
    }
}
