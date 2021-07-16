package com.silenteight.serp.governance.qa.manage.validation.details.dto;

import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import java.time.OffsetDateTime;

public interface AlertValidationDetailsDto {

  String getDiscriminator();

  DecisionState getState();

  String getDecisionBy();

  OffsetDateTime getDecisionAt();

  String getDecisionComment();

  OffsetDateTime getAddedAt();
}