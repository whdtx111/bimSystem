package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.WbsLv;

import java.util.List;
@Mapper
public interface WbsLvMapper extends BaseMapper<WbsLv> {

    WbsLv getById(String id);

    List<WbsLv> getWbsLvByWbsId(String wbsId);

    List<WbsLv> filterWbsLv(String code,String nameCn,String wbsId);

    boolean addWbsLv(WbsLv wbsLv);

    boolean updateWbsLv(WbsLv wbsLv);

    boolean deleteWbsLv(String id);
}
