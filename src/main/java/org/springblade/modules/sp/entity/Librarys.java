package org.springblade.modules.sp.entity;

import lombok.Data;

@Data
public class Librarys {

    private String[] libIds;
    private String key;
    private String value;

    public Librarys(String[] libIds, String key, String value) {
        this.libIds = libIds;
        this.key = key;
        this.value = value;
    }

    public Librarys() {
    }
}
