package org.springblade.modules.sp.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ElementLibraryDTO {

    private  JSONObject modelFile;
    private  JSONObject infoEncoding;
    private  List<JSONObject> propertyMgt;

    public ElementLibraryDTO(JSONObject modelFile,JSONObject infoEncoding,List<JSONObject> propertyMgt){
        this.modelFile = modelFile;
        this.infoEncoding = infoEncoding;
        this.propertyMgt = propertyMgt;
    }
    public ElementLibraryDTO() {};
}
