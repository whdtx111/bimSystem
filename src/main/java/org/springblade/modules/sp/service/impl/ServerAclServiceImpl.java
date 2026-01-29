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
import org.springblade.modules.sp.entity.ServerAcl;
import org.springblade.modules.sp.vo.ServerAclVO;
import org.springblade.modules.sp.mapper.ServerAclMapper;
import org.springblade.modules.sp.service.IServerAclService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 *  服务实现类
 *
 * @author wangc
 * @since 2023-09-13
 */
@Service
@DS("postgresql")
public class ServerAclServiceImpl extends ServiceImpl<ServerAclMapper, ServerAcl> implements IServerAclService {

	@Override
	public IPage<ServerAclVO> selectServerAclPage(IPage<ServerAclVO> page, ServerAclVO serverAcl) {
		return page.setRecords(baseMapper.selectServerAclPage(page, serverAcl));
	}

}
