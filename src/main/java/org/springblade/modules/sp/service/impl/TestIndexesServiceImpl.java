package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.TestIndexes;
import org.springblade.modules.sp.mapper.TestIndexesMapper;
import org.springblade.modules.sp.service.TestIndexesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 检测指标服务实现类
 *
 * @author Cascade
 * @since 2025-07-21
 */
@Service
@DS("postgresql")
public class TestIndexesServiceImpl extends BaseServiceImpl<TestIndexesMapper, TestIndexes> implements TestIndexesService {

    @Autowired
    private TestIndexesMapper testIndexesMapper;

    @Override
    public TestIndexes getTestIndexesById(String id) {
        try {
            return testIndexesMapper.getTestIndexesById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TestIndexes> getAll() {
        try {
            return testIndexesMapper.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TestIndexes> filterTestIndexes(String name, String auth, String source, Integer status) {
        try {
           List<TestIndexes> filterTestIndexes = testIndexesMapper.filterTestIndexes(name, auth, source, status);
            return filterTestIndexes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addTestIndexes(TestIndexes testIndexes) {
        try {
            if (StringUtils.isEmpty(testIndexes.getSource())){
                testIndexes.setSource("SMARTLINK");
            }
            return testIndexesMapper.addTestIndexes(testIndexes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertTestIndexesBatch(List<TestIndexes> list) {
        try {
            return testIndexesMapper.insertTestIndexesBatch(list);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTestIndexes(TestIndexes testIndexes) {
        try {
            return testIndexesMapper.updateTestIndexes(testIndexes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTestIndexesById(String id) {
        try {
            return testIndexesMapper.deleteTestIndexesById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
