package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.LibParameters;

import java.util.List;

public interface LibParametersService extends BaseService<LibParameters> {

    LibParameters getById(String id);

    List<LibParameters> getAllLibParameters();

    List<LibParameters> getAllLibParametersByWbsCode(String wbsCode);

    List<LibParameters> filterLibParameters(String wbsCode,String tempId,String libId);

    boolean updateLibParameters(LibParameters libParameters);

    boolean addLibParameters(LibParameters libParameters);

    boolean deleteLibParameters(@Param("id") String id);
}
