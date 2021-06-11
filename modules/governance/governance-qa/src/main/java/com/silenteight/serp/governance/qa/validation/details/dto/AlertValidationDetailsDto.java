package com.silenteight.serp.governance.qa.validation.details.dto;

import com.silenteight.serp.governance.qa.domain.DecisionState;

import java.time.OffsetDateTime;

public interface AlertValidationDetailsDto {

  String getAlertName();

  DecisionState getState();

  String getDecisionBy();

  OffsetDateTime getDecisionAt();

  String getDecisionComment();

  OffsetDateTime getAddedAt();
}