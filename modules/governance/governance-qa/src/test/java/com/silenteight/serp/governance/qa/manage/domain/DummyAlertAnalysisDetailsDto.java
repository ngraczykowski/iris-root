package com.silenteight.serp.governance.qa.manage.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.serp.governance.qa.manage.analysis.details.dto.AlertAnalysisDetailsDto;

import java.time.OffsetDateTime;

@Builder
@Data
class DummyAlertAnalysisDetailsDto implements AlertAnalysisDetailsDto {

  @NonNull
  String discriminator;
  @NonNull
  DecisionState state;
  OffsetDateTime decisionAt;
  String decisionBy;
  String decisionComment;
  @NonNull
  OffsetDateTime addedAt;
}
