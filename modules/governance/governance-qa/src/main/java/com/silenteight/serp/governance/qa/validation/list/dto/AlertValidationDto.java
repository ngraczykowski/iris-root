package com.silenteight.serp.governance.qa.validation.list.dto;

import com.silenteight.serp.governance.qa.domain.DecisionState;

import java.time.Instant;

public interface AlertValidationDto {

  String getAlertName();

  DecisionState getState();

  String getDecisionComment();

  String getDecisionBy();

  Instant getDecisionAt();

  Instant getAddedAt();
}
