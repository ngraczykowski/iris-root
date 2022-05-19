package com.silenteight.serp.governance.qa.manage.analysis.list.dto;

import com.silenteight.serp.governance.qa.manage.common.Tokenable;
import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import java.time.Instant;

import static java.lang.String.valueOf;

public interface AlertAnalysisDto extends Tokenable {

  String getAlertName();

  DecisionState getState();

  Instant getDecisionAt();

  String getDecisionBy();

  String getDecisionComment();

  Instant getAddedAt();

  @Override
  default String getToken() {
    return valueOf(getAddedAt());
  }
}
