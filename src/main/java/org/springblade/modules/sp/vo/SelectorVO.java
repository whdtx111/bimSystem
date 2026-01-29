package org.springblade.modules.sp.vo;

import lombok.Data;

@Data
public class SelectorVO {

    private String[] commitIds;
    private String streamId;

    public SelectorVO(String[] commitIds, String streamId) {
        this.commitIds = commitIds;
        this.streamId = streamId;
    }

    public SelectorVO() {
    }
}
