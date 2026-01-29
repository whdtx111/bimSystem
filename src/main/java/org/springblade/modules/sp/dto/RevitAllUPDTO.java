package org.springblade.modules.sp.dto;

import lombok.Data;

@Data
public class RevitAllUPDTO {

    private String id;
    private Integer isfinished;
    private String objId;

    public RevitAllUPDTO(String id, Integer isfinished, String objId) {
        this.id = id;
        this.isfinished = isfinished;
        this.objId = objId;
    }

    public RevitAllUPDTO() {
    }
}
