package org.springblade.modules.sp.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "NewBimListTreeVO对象", description = "NewBimListTreeVO对象")
public class NewBimListTreeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String type;
    private String family;
    private String category;
    private String units;
    private String elementId;
    private String builtInCategory;
    //    private JSONObject parameters;
    private String streamId;

//    private String lv;

//    public NewBimListTreeVO(String id, String type, String family, String category, String units, String elementId, String builtInCategory, String streamId, String lv) {
//        this.id = id;
//        this.type = type;
//        this.family = family;
//        this.category = category;
//        this.units = units;
//        this.elementId = elementId;
//        this.builtInCategory = builtInCategory;
////        this.parameters = new JSONObject();
//        this.streamId = streamId;
//    }
}
