package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.dto.LibDicDetailDTO;
import org.springblade.modules.sp.entity.LibDicDetail;

import java.util.List;

@Mapper
public interface LibDicDetailMapper extends BaseMapper<LibDicDetail> {

    List<LibDicDetail> getLibDicDetailByPid(@Param("pid") String pid);

    LibDicDetail getLibDicDetailById(String id);

    List<LibDicDetailDTO> listAllWithCode();

    boolean addLibDicDetail(LibDicDetail libDicDetail);

    boolean addLibDicDetailList(List<LibDicDetail> libDicDetails);

    boolean updateLibDicDetail(LibDicDetail libDicDetail);

    boolean updateLibDicDetailStatus(String id,Integer status);

    boolean deleteLibDicDetailByPid(String pid);

    boolean deleteLibDicDetailById(String id);
}
