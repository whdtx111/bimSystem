package org.springblade.modules.sp.vo;

import lombok.Data;

@Data
public class LibraryVO {

    private String[] ids;
    private String key;
    private String value;

    public LibraryVO(String[] ids, String key, String value) {
        this.ids = ids;
        this.key = key;
        this.value = value;
    }

    public LibraryVO() {
    }
}
