package com.silenteight.serp.governance.qa.manage.validation.list.dto;

import com.silenteight.serp.governance.qa.manage.common.Tokenable;
import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import java.time.Instant;

import static java.lang.String.valueOf;

public interface AlertValidationDto extends Tokenable {

  String getAlertName();

  DecisionState getState();

  String getDecisionComment();

  String getDecisionBy();

  Instant getDecisionAt();

  Instant getAddedAt();

  default String getToken() {
    return valueOf(getAddedAt());
  }
}
