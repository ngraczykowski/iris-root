package com.silenteight.sens.webapp.backend.presentation.dto.decisiontree.details;

import lombok.Data;

import com.silenteight.sens.webapp.backend.presentation.dto.common.StatisticGroupDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class DecisionTreeSummaryDto {

  private final List<StatisticGroupDto> statGroups = new ArrayList<>();
}
