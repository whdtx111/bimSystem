package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springblade.modules.sp.entity.ElementParameter;

import java.util.List;

@Mapper
public interface ElementParameterMapper extends BaseMapper<ElementParameter> {

    //根据id查询
    @Select("select * from sp_element_parameter where id = #{id}")
    ElementParameter getById(@Param("id") String id);

    @Select("<script>" +
            "SELECT * FROM sp_element_parameter " +
            "WHERE 1=1 " +
            "<if test='ids != null and ids.size > 0'>" +
            "AND id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</if>" +
            "</script>")
    List<ElementParameter> getByIds(@Param("ids") List<String> ids);

    //insert
    @Insert("insert into sp_element_parameter(id,pkey,name,units,category) values(#{id},#{pkey},#{name},#{units},#{category})")
    Integer addElementParameter(@Param("id") String id,@Param("pkey") String pkey,@Param("name") String name, @Param("units") String units, @Param("category") String category);

    //update
    @Update("update sp_element_parameter set pkey = #{pkey}, name = #{name},units = #{units},category = #{category} where id = #{id}")
    Integer  updateElementParameter(@Param("id") String id,@Param("pkey") String pkey, @Param("name") String name, @Param("units") String units, @Param("category") String category);

    //delete
    @Delete("delete from sp_element_parameter where id = #{id}")
    Integer deleteElementParameter(@Param("id") String id);

}
