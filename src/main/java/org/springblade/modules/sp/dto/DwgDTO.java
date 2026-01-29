package org.springblade.modules.sp.dto;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import lombok.Data;

import java.util.List;

/**
 * @author huang can/dengtx
 * @since 2024/3/19 11:04
 */

@Data
public class DwgDTO {

    private String projectId;
    private String name;
    private String mapId;
    private JSONObject detail;
    private String modifyUser;

    private List<DwgDTO> childrenDTO;
}
