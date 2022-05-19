package com.silenteight.serp.governance.qa.manage.domain.dto;

import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import java.time.Instant;

public interface AlertDto {

  String getAlertName();

  DecisionState getState();

  Instant getDecisionAt();

  String getDecisionBy();

  String getDecisionComment();

  Instant getAddedAt();
}
