package org.springblade.modules.sp.dto;

import lombok.Data;
import org.springblade.modules.sp.entity.RoleAuth;

@Data
public class RoleAuthStreamDTO extends RoleAuth {

    private String streamId;
}
