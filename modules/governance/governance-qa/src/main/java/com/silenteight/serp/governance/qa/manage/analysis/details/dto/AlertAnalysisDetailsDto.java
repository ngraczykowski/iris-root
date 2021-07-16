package com.silenteight.serp.governance.qa.manage.analysis.details.dto;

import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import java.time.OffsetDateTime;

public interface AlertAnalysisDetailsDto {

  String getDiscriminator();

  DecisionState getState();

  OffsetDateTime getDecisionAt();

  String getDecisionBy();

  String getDecisionComment();

  OffsetDateTime getAddedAt();
}