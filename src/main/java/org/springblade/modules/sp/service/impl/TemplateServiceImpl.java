package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.modules.sp.entity.TemplateDO;
import org.springblade.modules.sp.mapper.TemplateMapper;
import org.springblade.modules.sp.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据模板接口
 * @author huang can/dengtx
 * @since 2024/3/19 12:02
 */

@Service
@DS("postgresql")
public class TemplateServiceImpl implements ITemplateService {

    @Autowired
    private TemplateMapper templateMapper;

    /**
     * 获取列表
     * @param branchId
     * @return
     */
    @Override
    public List<TemplateDO> getTemplateList(String branchId,String streamId) {
        try {
            List<TemplateDO> templateList = templateMapper.getTemplateList(branchId,streamId);
            return templateList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TemplateDO getById(String id) {
        try {
            TemplateDO byId = templateMapper.getById(id);
            return byId;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加树节点
     * @param templateDO
     * @return
     */
    @Override
    public boolean addTemplateNode(TemplateDO templateDO) {
        try {
            if (ObjectUtils.isEmpty(templateDO)){
                return false;
            }
            return templateMapper.addTemplateNode(templateDO);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除树节点
     * @param id
     * @return
     */
    @Override
    public boolean deleteTemplateNode(String id){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
          return templateMapper.deleteTemplateNode(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改树节点
     * @param templateDO
     * @return
     */
    @Override
    public boolean updateTemplateNode(TemplateDO templateDO){
        try {
            if (ObjectUtils.isEmpty(templateDO)){
                return false;
            }
            return templateMapper.updateTemplateNode(templateDO);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
