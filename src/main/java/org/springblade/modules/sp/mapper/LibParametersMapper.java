package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.LibParameters;
import org.springblade.modules.sp.entity.Library;

import java.util.List;
@Mapper
public interface LibParametersMapper extends BaseMapper<LibParameters> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    LibParameters getById(String id);

    List<LibParameters> getAllLibParameters();

    List<LibParameters> getAllLibParametersByWbsCode(String wbsCode);

    List<LibParameters> filterLibParameters(String wbsCode,String tempId,String libId);

    boolean addLibParameters(LibParameters libParameters);

    boolean updateLibParameters(LibParameters libParameters);

    boolean deleteLibParameters(@Param("id") String id);

}
