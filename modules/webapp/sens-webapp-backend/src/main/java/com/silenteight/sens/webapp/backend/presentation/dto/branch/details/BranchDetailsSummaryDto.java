package com.silenteight.sens.webapp.backend.presentation.dto.branch.details;

import lombok.Data;

import com.silenteight.sens.webapp.backend.presentation.dto.common.StatisticGroupDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class BranchDetailsSummaryDto {

  private final List<StatisticGroupDto> statGroups = new ArrayList<>();
}
