package org.springblade.modules.sp.entity;

import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;
@Data
public class LibraryTag extends BaseEntity{

    private String id;
    private String tag;
    private String tagColor;

    public LibraryTag() {
        this.id = UUID.randomUUID().toString();
    }

    public LibraryTag(String id, String tag, String tagColor) {
        this.id = UUID.randomUUID().toString();
        this.tag = tag;
        this.tagColor = tagColor;
    }
}
