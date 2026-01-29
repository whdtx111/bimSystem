package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.WbsParametersCopy;
import org.springblade.modules.sp.entity.WbsParametersCopyColor;

import java.util.List;

public interface WbsParametersCopyColorService extends BaseService<WbsParametersCopyColor> {

    WbsParametersCopyColor getByWbsCode(String wbsCode,String wbsId);

    List<WbsParametersCopyColor> getAllWbsParametersCopyColorByWbsId(String wbsId);

    boolean addWbsParametersCopyColor(WbsParametersCopyColor wbsParametersCopyColor);

    boolean addWbsParametersCopyColorList(List<WbsParametersCopyColor> list);

    boolean updateWbsParametersCopyColor(WbsParametersCopyColor wbsParametersCopyColor);

    boolean deleteWbsParametersCopyColor(String id);
}
