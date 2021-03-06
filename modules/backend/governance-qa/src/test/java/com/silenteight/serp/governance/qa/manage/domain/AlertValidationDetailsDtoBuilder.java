package com.silenteight.serp.governance.qa.manage.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.serp.governance.qa.manage.validation.details.dto.AlertValidationDetailsDto;

import java.time.OffsetDateTime;

@Builder
@Data
class AlertValidationDetailsDtoBuilder implements AlertValidationDetailsDto {

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
