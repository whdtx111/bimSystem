package org.springblade.modules.sp.service;

import com.github.pagehelper.Page;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.RulesLibrary;
import org.springblade.modules.sp.vo.RulesDetailVO;
import org.springblade.modules.sp.vo.RulesLibraryDetailVO;
import org.springblade.modules.sp.vo.RulesLibraryVO;

import java.util.List;


public interface RulesLibraryService extends BaseService<RulesLibrary> {

    RulesLibrary getRulesLibraryById(String id);

    List<RulesLibrary> getAll();

    Page<RulesLibrary> filterRulesLibrary(String name, String auth, String source, Integer status, Integer pageSize, Integer currentPage);

    boolean addRulesLibrary(RulesLibrary rulesLibrary);

    boolean insertRulesLibraryBatch(List<RulesLibrary> list);

    boolean updateRulesLibrary(RulesLibrary rulesLibrary);

    boolean deleteRulesLibraryById(String id);

    RulesLibraryDetailVO getRulesLibraryWithDetails(String id);

    List<RulesLibraryDetailVO> getAllRulesLibraryWithDetails(String streamId);

    boolean saveRulesDetails(RulesLibraryVO vo);

}
