package org.springblade.modules.sp.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class UserInfoVO {

    private String id;
    private String name;
    private String email;
    private String company;
    private String avatar;
    private JSONObject streams;
    private JSONObject commits;
}
