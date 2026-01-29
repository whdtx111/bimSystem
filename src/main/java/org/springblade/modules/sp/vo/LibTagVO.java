package org.springblade.modules.sp.vo;

import lombok.Data;

@Data
public class LibTagVO {

    private String[] ids;
    private String tag;
    private String tagColor;

    public LibTagVO(String[] ids, String tag, String tagColor) {
        this.ids = ids;
        this.tag = tag;
        this.tagColor = tagColor;
    }

    public LibTagVO() {
    }
}
