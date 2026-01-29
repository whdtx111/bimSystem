package org.springblade.modules.sp.vo;

import lombok.Data;

@Data
public class RoleAuthDetailVO {

    private String authName;

    private String authLevel;

    public RoleAuthDetailVO(String authName, String authLevel) {
        this.authName = authName;
        this.authLevel = authLevel;
    }
    public RoleAuthDetailVO() {}
}
