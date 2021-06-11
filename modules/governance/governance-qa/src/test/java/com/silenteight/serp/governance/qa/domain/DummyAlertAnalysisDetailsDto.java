package com.silenteight.serp.governance.qa.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.serp.governance.qa.analysis.details.dto.AlertAnalysisDetailsDto;

import java.time.OffsetDateTime;

@Builder
@Data
class DummyAlertAnalysisDetailsDto implements AlertAnalysisDetailsDto {

  @NonNull
  String alertName;
  @NonNull
  DecisionState state;
  OffsetDateTime decisionAt;
  String decisionBy;
  String decisionComment;
  @NonNull
  OffsetDateTime addedAt;
}
