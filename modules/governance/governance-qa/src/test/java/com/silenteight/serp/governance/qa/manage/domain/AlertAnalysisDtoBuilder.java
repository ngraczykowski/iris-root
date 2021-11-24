package com.silenteight.serp.governance.qa.manage.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.serp.governance.qa.manage.analysis.list.dto.AlertAnalysisDto;

import java.time.Instant;

@Builder
@Data
class AlertAnalysisDtoBuilder implements AlertAnalysisDto {

  @NonNull
  String alertName;
  @NonNull
  DecisionState state;
  Instant decisionAt;
  String decisionBy;
  String decisionComment;
  @NonNull
  Instant addedAt;
}
