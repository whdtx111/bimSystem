package org.springblade.modules.sp.vo;

import lombok.Data;

import java.util.List;

@Data
public class ObjFacesVO {

    private String streamId;
    private List<String> objIdsA;
    private List<String> objIdsB;

    public ObjFacesVO(String streamId, List<String> objIdsA, List<String> objIdsB) {
        this.streamId = streamId;
        this.objIdsA = objIdsA;
        this.objIdsB = objIdsB;
    }

    public ObjFacesVO() {}
}
