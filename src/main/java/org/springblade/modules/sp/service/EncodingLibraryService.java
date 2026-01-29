package org.springblade.modules.sp.service;

import com.github.pagehelper.Page;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.EncodingLibrary;

import java.util.List;

public interface EncodingLibraryService extends BaseService<EncodingLibrary> {

    EncodingLibrary getById(String id);

    List<EncodingLibrary> getAll();

    List<EncodingLibrary> getAllEncodingLibraries(String name, String code, String lv, Integer status);

    Page<EncodingLibrary> filterEncodingLibrary(String name, String code, String lv, Integer status,Integer pageSize, Integer currentPage);

    boolean addEncodingLibrary(EncodingLibrary encodingLibrary);

    boolean addEncodingLibraryList(List<EncodingLibrary> encodingLibrary);

    boolean updateEncodingLibrary(EncodingLibrary encodingLibrary);

    boolean deleteEncodingLibrary(String id);

}
