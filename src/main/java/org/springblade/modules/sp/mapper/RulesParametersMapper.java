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
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.RulesParameters;

import java.util.List;

/**
 * 4a_用户 Mapper 接口
 *
 * @author wangc
 * @since 2023-09-13
 */
@Mapper
public interface RulesParametersMapper extends BaseMapper<RulesParameters> {

    RulesParameters getRulesParametersById(String id);

//    List<RulesParameters> getRulesParametersByDetailId(String detailId);

    List<RulesParameters> getAll();

    boolean addRulesParameters(RulesParameters rulesParameters);

    boolean insertRulesParametersBatch(List<RulesParameters> list);

    boolean updateRulesParameters(RulesParameters rulesParameters);

    boolean deleteRulesParametersById(String id);

    boolean deleteRulesParametersByDetailId(String detailId);
}
