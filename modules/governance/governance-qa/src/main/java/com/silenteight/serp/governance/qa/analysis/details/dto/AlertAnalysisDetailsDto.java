package com.silenteight.serp.governance.qa.analysis.details.dto;

import com.silenteight.serp.governance.qa.domain.DecisionState;

import java.time.OffsetDateTime;

public interface AlertAnalysisDetailsDto {

  String getAlertName();

  DecisionState getState();

  OffsetDateTime getDecisionAt();

  String getDecisionBy();

  String getDecisionComment();

  OffsetDateTime getAddedAt();
}