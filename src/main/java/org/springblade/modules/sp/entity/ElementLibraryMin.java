package org.springblade.modules.sp.entity;

import lombok.Data;

import java.util.Date;
@Data
public class ElementLibraryMin {

    private String id;
    private String name;
    private String modifyUser;
    private Date modifyTime;
    private Date createTime;
    private Integer status;

    public ElementLibraryMin(String id, String name, String modifyUser, Date modifyTime, Date createTime, Integer status) {
        this.id = id;
        this.name = name;
        this.modifyUser = modifyUser;
        this.modifyTime = modifyTime;
        this.createTime = createTime;
        this.status = status;
    }

    public ElementLibraryMin() {}
}
