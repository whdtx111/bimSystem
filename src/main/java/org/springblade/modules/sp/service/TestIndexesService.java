package org.springblade.modules.sp.service;

import com.github.pagehelper.Page;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.TestIndexes;

import java.util.List;

/**
 * 检测指标服务接口
 *
 * @author Cascade
 * @since 2025-07-21
 */
public interface TestIndexesService extends BaseService<TestIndexes> {

    TestIndexes getTestIndexesById(String id);

    List<TestIndexes> getAll();

    List<TestIndexes> filterTestIndexes(String name, String auth, String source, Integer status);

    boolean addTestIndexes(TestIndexes testIndexes);

    boolean insertTestIndexesBatch(List<TestIndexes> list);

    boolean updateTestIndexes(TestIndexes testIndexes);

    boolean deleteTestIndexesById(String id);
}
