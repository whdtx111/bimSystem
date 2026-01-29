package org.springblade.modules.sp.vo;

import lombok.Data;
import org.springblade.modules.sp.entity.RulesParametersLOD;

import java.util.List;

@Data
public class RulesLibraryVO {
    private String id;
    private List<RulesDetailVO> rulesDetails;
    private List<RulesParametersLOD> rulesParametersLOD;
}