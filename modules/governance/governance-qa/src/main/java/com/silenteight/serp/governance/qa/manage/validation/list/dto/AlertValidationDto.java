package com.silenteight.serp.governance.qa.manage.validation.list.dto;

import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import java.time.Instant;

public interface AlertValidationDto {

  String getDiscriminator();

  DecisionState getState();

  String getDecisionComment();

  String getDecisionBy();

  Instant getDecisionAt();

  Instant getAddedAt();
}
