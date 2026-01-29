package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.RevitRef;

import java.util.List;

public interface RevitRefService extends BaseService<RevitRef> {

    RevitRef getById(@Param("id") String id);

    List<RevitRef> getAllByGroup(@Param("group") String group);

    boolean addRevitRef(RevitRef revitRef);


}
