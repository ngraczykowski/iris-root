package com.silenteight.serp.governance.qa.manage.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.serp.governance.qa.manage.validation.list.dto.AlertValidationDto;

import java.time.Instant;

@Builder
@Data
class AlertValidationDtoBuilder implements AlertValidationDto {

  @NonNull
  String discriminator;
  @NonNull
  DecisionState state;
  Instant decisionAt;
  String decisionBy;
  String decisionComment;
  @NonNull
  Instant addedAt;
}
