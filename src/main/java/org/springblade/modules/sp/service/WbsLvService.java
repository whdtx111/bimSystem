package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.WbsLv;

import java.util.List;

public interface WbsLvService extends BaseService<WbsLv> {

    WbsLv getById(String id);

    List<WbsLv> getWbsLvByWbsId(String wbsId);

    List<WbsLv> filterWbsLv(String code,String nameCn,String wbsId);

    boolean addWbsLv(WbsLv wbsLv);

    boolean updateWbsLv(WbsLv wbsLv);

    boolean deleteWbsLv(String id);

}
