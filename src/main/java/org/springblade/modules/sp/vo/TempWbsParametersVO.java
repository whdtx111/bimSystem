package org.springblade.modules.sp.vo;

import lombok.Data;

@Data
public class TempWbsParametersVO {

    private String branchId;
    private String tempId;
    private String pid;
    private String wbsId;
    private String wbsCode;
    private String wbsName;
    private String wbsLv;
    private String modifyUser;

    public TempWbsParametersVO(String branchId, String tempId, String pid, String wbsId, String wbsCode, String wbsName, String wbsLv, String modifyUser) {
        this.branchId = branchId;
        this.tempId = tempId;
        this.pid = pid;
        this.wbsId = wbsId;
        this.wbsCode = wbsCode;
        this.wbsName = wbsName;
        this.wbsLv = wbsLv;
        this.modifyUser = modifyUser;
    }

    public TempWbsParametersVO() {
    }
}
