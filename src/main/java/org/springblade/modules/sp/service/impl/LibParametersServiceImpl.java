package org.springblade.modules.sp.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.LibParameters;
import org.springblade.modules.sp.mapper.LibParametersMapper;
import org.springblade.modules.sp.service.LibParametersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class LibParametersServiceImpl extends BaseServiceImpl<LibParametersMapper, LibParameters> implements LibParametersService {

    @Autowired
    private LibParametersMapper libParametersMapper;

    @Override
    public LibParameters getById(String id){
       try {
           return libParametersMapper.getById(id);
       }catch (Exception e){
           e.printStackTrace();
           return null;
       }
    }


    @Override
    public List<LibParameters> getAllLibParameters(){
       try {
           return libParametersMapper.getAllLibParameters();
       }catch (Exception e){
           e.printStackTrace();
           return null;
       }
    }

    @Override
    public boolean addLibParameters(LibParameters libParameters){
       try {
           return libParametersMapper.addLibParameters(libParameters);
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }
    }
    @Override
    public boolean deleteLibParameters(String id){
       try {
           return libParametersMapper.deleteLibParameters(id);
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }
    }

    @Override
    public boolean updateLibParameters(LibParameters libParameters){
       try {
           return libParametersMapper.updateLibParameters(libParameters);
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }
    }

    @Override
    public List<LibParameters> getAllLibParametersByWbsCode(String wbsCode){
       try {
           return libParametersMapper.getAllLibParametersByWbsCode(wbsCode);
       }catch (Exception e){
           e.printStackTrace();
           return null;
       }
    }

    @Override
    public List<LibParameters> filterLibParameters(String wbsCode,String tempId,String libId){
       try {
           return libParametersMapper.filterLibParameters(wbsCode,tempId,libId);
       }catch (Exception e){
           e.printStackTrace();
           return null;
       }
    }

}
