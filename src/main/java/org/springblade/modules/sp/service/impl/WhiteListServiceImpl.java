package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.WhiteList;
import org.springblade.modules.sp.mapper.WhiteListMapper;
import org.springblade.modules.sp.service.WhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 白名单Service实现类
 */
@Slf4j
@Service
@DS("postgresql")
public class WhiteListServiceImpl extends BaseServiceImpl<WhiteListMapper, WhiteList> implements WhiteListService {

    @Autowired
    private WhiteListMapper whiteListMapper;

    @Override
    public WhiteList getById(String id) {
        try {
            return whiteListMapper.getById(id);
        } catch (Exception e) {
            log.error("查询白名单记录失败, id={}", id, e);
        }
        return null;
    }

    @Override
    public List<WhiteList> getAll() {
        try {
            return whiteListMapper.getAll();
        } catch (Exception e) {
            log.error("查询所有白名单记录失败", e);
        }
        return null;
    }

    @Override
    public List<WhiteList> getByTemplateId(String templateId,String streamId,String branchId) {
        try {
            return whiteListMapper.getByTemplateId(templateId,streamId,branchId);
        } catch (Exception e) {
            log.error("根据模板ID查询白名单记录失败, templateId={}, branchId={}", templateId, branchId, e);
        }
        return null;
    }

    @Override
    public List<WhiteList> getByFileId(String fileId) {
        try {
            return whiteListMapper.getByFileId(fileId);
        } catch (Exception e) {
            log.error("根据文件ID查询白名单记录失败, fileId={}", fileId, e);
        }
        return null;
    }

    @Override
    public List<WhiteList> getByName(String name) {
        try {
            return whiteListMapper.getByName(name);
        } catch (Exception e) {
            log.error("根据名称查询白名单记录失败, name={}", name, e);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addWhiteList(WhiteList whiteList) {
        try {
            whiteList.setModifyTime(new Date());
            if (whiteList.getCreateTime() == null) {
                whiteList.setCreateTime(new Date());
            }
            int result = whiteListMapper.insert(whiteList);
            log.info("新增白名单记录成功, id={}, name={}", whiteList.getId(), whiteList.getName());
            return result > 0;
        } catch (Exception e) {
            log.error("新增白名单记录失败", e);
            throw new RuntimeException("新增白名单记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAddWhiteList(List<WhiteList> whiteListList) {
        try {
            if (whiteListList == null || whiteListList.isEmpty()) {
                log.warn("批量新增白名单记录失败: 列表为空");
                return false;
            }
            
            // 设置时间
            Date now = new Date();
            for (WhiteList whiteList : whiteListList) {
                if (whiteList.getCreateTime() == null) {
                    whiteList.setCreateTime(now);
                }
                whiteList.setModifyTime(now);
            }
            
            boolean result = whiteListMapper.batchInsert(whiteListList);
            log.info("批量新增白名单记录成功, 数量={}", whiteListList.size());
            return result;
        } catch (Exception e) {
            log.error("批量新增白名单记录失败", e);
            throw new RuntimeException("批量新增白名单记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWhiteList(WhiteList whiteList) {
        try {
            whiteList.setModifyTime(new Date());
            boolean result = whiteListMapper.updateWhiteList(whiteList);
            log.info("更新白名单记录成功, id={}", whiteList.getId());
            return result;
        } catch (Exception e) {
            log.error("更新白名单记录失败", e);
            throw new RuntimeException("更新白名单记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        try {
            boolean result = whiteListMapper.deleteById(id);
            log.info("删除白名单记录成功, id={}", id);
            return result;
        } catch (Exception e) {
            log.error("删除白名单记录失败, id={}", id, e);
            throw new RuntimeException("删除白名单记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByTemplateId(String templateId) {
        try {
            boolean result = whiteListMapper.deleteByTemplateId(templateId);
            log.info("根据模板ID删除白名单记录成功, templateId={}", templateId);
            return result;
        } catch (Exception e) {
            log.error("根据模板ID删除白名单记录失败, templateId={}", templateId, e);
            throw new RuntimeException("根据模板ID删除白名单记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByFileId(String fileId) {
        try {
            boolean result = whiteListMapper.deleteByFileId(fileId);
            log.info("根据文件ID删除白名单记录成功, fileId={}", fileId);
            return result;
        } catch (Exception e) {
            log.error("根据文件ID删除白名单记录失败, fileId={}", fileId, e);
            throw new RuntimeException("根据文件ID删除白名单记录失败: " + e.getMessage());
        }
    }
}
