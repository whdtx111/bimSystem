package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;

import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.TemplatePartElement;
import org.springblade.modules.sp.mapper.TemplatePartElementMapper;
import org.springblade.modules.sp.service.TemplatePartElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class TemplatePartElementServiceImpl extends BaseServiceImpl<TemplatePartElementMapper,TemplatePartElement> implements TemplatePartElementService {

    @Autowired
    private TemplatePartElementMapper templatePartElementMapper;

    @Override
    public TemplatePartElement getByTemplateId(String templateId) {
       try{
           return templatePartElementMapper.getByTemplateId(templateId);
       }catch (Exception e){
           e.printStackTrace();
           return null;
       }
    }

    @Override
    public boolean addTemplatePartElement(TemplatePartElement templatePartElement) {
        try{
            return templatePartElementMapper.addTemplatePartElement(templatePartElement);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTemplatePartElement(TemplatePartElement templatePartElement) {
        try{
            return templatePartElementMapper.updateTemplatePartElement(templatePartElement);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTemplatePartElement(String templateId) {
        try{
            return templatePartElementMapper.deleteTemplatePartElement(templateId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<TemplatePartElement> getLatestTwoByTemplateId(String templateId) {
        try{
            return templatePartElementMapper.getLatestTwoByTemplateId(templateId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
