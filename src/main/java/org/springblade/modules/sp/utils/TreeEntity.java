package org.springblade.modules.sp.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class TreeEntity {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

	/**
	 * code
	 */
	private String wbsCode;

	/**
	 * 级别
	 */
	private String typeLevel;


}
