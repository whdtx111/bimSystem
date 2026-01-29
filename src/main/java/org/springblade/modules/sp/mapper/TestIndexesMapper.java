package org.springblade.modules.sp.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.TestIndexes;

import java.util.List;

/**
 * 检测指标 Mapper 接口
 *
 * @author Cascade
 * @since 2025-07-21
 */
@Mapper
public interface TestIndexesMapper extends BaseMapper<TestIndexes> {

    TestIndexes getTestIndexesById(String id);

    List<TestIndexes> getAll();

    List<TestIndexes> filterTestIndexes(String name, String auth, String source, Integer status);

    boolean addTestIndexes(TestIndexes testIndexes);

    boolean insertTestIndexesBatch(List<TestIndexes> list);

    boolean updateTestIndexes(TestIndexes testIndexes);

    boolean deleteTestIndexesById(String id);
}
