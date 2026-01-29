package org.springblade.modules.sp.vo;

import lombok.Data;

@Data
public class XiasanshotCountVO {

    private String checkStatus;
    private String count;

    public XiasanshotCountVO(String checkStatus, String count) {
        this.checkStatus = checkStatus;
        this.count = count;
    }

    public XiasanshotCountVO() {
    }
}
