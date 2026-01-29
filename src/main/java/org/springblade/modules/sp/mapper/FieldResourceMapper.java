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
package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.FieldResource;
import org.springblade.modules.sp.entity.RulesLibrary;
import org.springblade.modules.sp.vo.RulesLibraryDetailVO;

import java.util.List;

/**
 * 4a_用户 Mapper 接口
 *
 * @author wangc
 * @since 2023-09-13
 */
@Mapper
public interface FieldResourceMapper extends BaseMapper<FieldResource> {

    FieldResource getById(String id);

    List<FieldResource> getByTag(String tag);

    List<FieldResource> getAll();

    boolean addFieldResource(FieldResource fieldResource);

    boolean updateFieldResource(FieldResource fieldResource);

    boolean deleteById(String id);

    /**
     * 获取最大的code编号
     * @return 最大编号，如果没有数据返回0
     */
    Integer getMaxCodeNumber();

}
