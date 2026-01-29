package org.springblade.modules.sp.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "user对象", description = "搜索用的对象")
public class SearchUserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalOwnedStreamsFavorites;
    private String id;
    private String name;
    private String bio;
    private String company;
    private String avatar;
    private boolean verified;
    private String role;

}
