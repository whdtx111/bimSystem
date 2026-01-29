package org.springblade.modules.sp.vo;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springblade.modules.sp.entity.TestIndexes;

/**
 * 检测指标分页对象
 *
 * @author Cascade
 * @since 2025-07-21
 */
@Data
public class PageTestIndexes {
    
    private Page<TestIndexes> pageTestIndexes;
    
    private long total;
}
