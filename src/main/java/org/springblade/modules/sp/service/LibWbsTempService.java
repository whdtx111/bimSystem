package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.LibWbsTemp;

import java.util.List;

public interface LibWbsTempService extends BaseService<LibWbsTemp> {

    LibWbsTemp getById(String id);

    LibWbsTemp getLibWbsTemp(String wbsCode, String tempId);

    List<String> getWbsCodeByTempId(String tempId);

    List<LibWbsTemp> getLibWbsTempByTempId(String tempId);

    boolean addLibWbsTemp(LibWbsTemp libWbsTemp);

    boolean updateLibWbsTemp(LibWbsTemp libWbsTemp);

    boolean deleteLibWbsTemp(String id);
}
