package org.springblade.modules.sp.vo;

import lombok.Data;
import org.springblade.modules.sp.entity.RulesLibrary;
import org.springblade.modules.sp.entity.RulesParametersLOD;

import java.util.List;

@Data
public class RulesLibraryDetailVO extends RulesLibrary {
    private List<RulesDetailVO> rulesDetails;
    private List<RulesParametersLOD> rulesParametersLOD;
}