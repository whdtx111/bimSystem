package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.dto.LibDicDetailDTO;
import org.springblade.modules.sp.entity.LibDicDetail;

import java.util.List;

public interface LibDicDetailService extends BaseService<LibDicDetail> {

    List<LibDicDetail> getLibDicDetailByPid(@Param("pid") String pid);

    List<LibDicDetailDTO> listAllWithCode();

    LibDicDetail getLibDicDetailById(String id);

    boolean addLibDicDetail(LibDicDetail libDicDetail);

    boolean addLibDicDetailList(List<LibDicDetail> libDicDetails);

    boolean updateLibDicDetail(LibDicDetail libDicDetail);

    boolean updateLibDicDetailStatus(String id,Integer status);

    boolean deleteLibDicDetailByPid(String pid);

    boolean deleteLibDicDetailById(String id);
}
