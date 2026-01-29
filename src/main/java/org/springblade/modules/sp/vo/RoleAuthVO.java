package org.springblade.modules.sp.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class RoleAuthVO {

    private static final long serialVersionUID = 1L;

    private String roleId;

    private String modifyUser;

    private List<RoleAuthDetailVO> roleAuthDetailVOList;

    public RoleAuthVO(String roleId, List<RoleAuthDetailVO> roleAuthDetailVOList) {
        this.roleId = roleId;
        this.roleAuthDetailVOList = roleAuthDetailVOList;
    }
    public RoleAuthVO() {}
}
