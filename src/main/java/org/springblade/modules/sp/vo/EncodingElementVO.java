package org.springblade.modules.sp.vo;

import lombok.Data;

@Data
public class EncodingElementVO {

    private String streamId;
    private String[] elementId;
    private String branchId;
    private String encodingId;

    public EncodingElementVO(String streamId, String[] elementId, String branchId, String encodingId) {
        this.streamId = streamId;
        this.elementId = elementId;
        this.branchId = branchId;
        this.encodingId = encodingId;
    }
    public EncodingElementVO(){}
}
