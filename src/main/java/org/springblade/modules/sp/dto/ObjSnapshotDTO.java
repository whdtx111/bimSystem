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
package org.springblade.modules.sp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springblade.modules.sp.entity.ObjSnapshot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 模型 色值 和 剖切快照数据传输对象实体类
 *
 * @author wangc
 * @since 2023-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ObjSnapshotDTO extends ObjSnapshot {
	private static final long serialVersionUID = 1L;
	private String userName;
	@DateTimeFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private Date createTime;

}
