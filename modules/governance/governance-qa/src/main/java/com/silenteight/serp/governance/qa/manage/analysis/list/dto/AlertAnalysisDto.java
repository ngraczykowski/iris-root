package com.silenteight.serp.governance.qa.manage.analysis.list.dto;

import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import java.time.Instant;

public interface AlertAnalysisDto {

  String getAlertName();

  DecisionState getState();

  Instant getDecisionAt();

  String getDecisionBy();

  String getDecisionComment();

  Instant getAddedAt();
}
