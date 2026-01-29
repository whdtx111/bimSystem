package org.springblade.modules.sp.vo;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springblade.modules.sp.entity.Library;

@Data
public class PageLib {

    private Page<Library> pageLib;
    private Long total;

    public PageLib(Page<Library> pageLib, Long total) {
        this.pageLib = pageLib;
        this.total = total;
    }

    public PageLib() {
    }
}
