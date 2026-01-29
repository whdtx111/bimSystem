/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.sp.entity.FieldResource;
import org.springblade.modules.sp.mapper.FieldResourceMapper;
import org.springblade.modules.sp.service.FieldResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Wbs任务表 服务实现类
 *
 * @author wangc
 * @since 2023-04-23
 */
@Service
@DS("postgresql")
public class FieldResourceServiceImpl extends ServiceImpl<FieldResourceMapper, FieldResource> implements FieldResourceService {

    @Autowired
    private FieldResourceMapper fieldResourceMapper;

    @Override
    public FieldResource getById(String id) {
        try {
            return fieldResourceMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<FieldResource> getByTag(String tag) {
        try {
            return fieldResourceMapper.getByTag(tag);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<FieldResource> getAll() {
        try {
            return fieldResourceMapper.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addFieldResource(FieldResource fieldResource) {
        try {
            // 如果code为空，自动生成
            if (fieldResource.getCode() == null || fieldResource.getCode().trim().isEmpty()) {
                fieldResource.setCode(generateCode());
            }
            boolean b = fieldResourceMapper.addFieldResource(fieldResource);
            if (b) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateFieldResource(FieldResource fieldResource) {
        try {
            boolean b = fieldResourceMapper.updateFieldResource(fieldResource);
            if (b) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(String id) {
        try {
            boolean b = fieldResourceMapper.deleteById(id);
            if (b) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 生成自动递增的code
     * @return 生成的code，格式为CustomFields_1, CustomFields_2...
     */
    private String generateCode() {
        Integer maxNumber = baseMapper.getMaxCodeNumber();
        int nextNumber = (maxNumber == null ? 0 : maxNumber) + 1;
        return "CustomFields_" + nextNumber;
    }

}


