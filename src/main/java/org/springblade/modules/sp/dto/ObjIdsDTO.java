package org.springblade.modules.sp.dto;

import lombok.Data;

/**
 * objIds参数包装类
 */
@Data
public class ObjIdsDTO {
    private String streamId;
    private String branchId;
    private String[] objIds;
}