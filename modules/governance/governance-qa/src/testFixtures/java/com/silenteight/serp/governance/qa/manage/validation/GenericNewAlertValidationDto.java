package com.silenteight.serp.governance.qa.manage.validation;

import com.silenteight.serp.governance.qa.manage.domain.DecisionState;
import com.silenteight.serp.governance.qa.manage.validation.list.dto.AlertValidationDto;

import java.time.Instant;

import static com.silenteight.serp.governance.qa.DecisionFixture.*;

public class GenericNewAlertValidationDto implements AlertValidationDto {

  private final String alertName = getAlertName();

  private final Instant addedAt = Instant.now();

  private final DecisionState state = STATE_NEW;

  private final Instant decisionAt = Instant.now();

  @Override
  public String getAlertName() {
    return alertName;
  }

  @Override
  public DecisionState getState() {
    return state;
  }

  @Override
  public String getDecisionComment() {
    return COMMENT_FAILED;
  }

  @Override
  public Instant getDecisionAt() {
    return decisionAt;
  }

  @Override
  public String getDecisionBy() {
    return DECIDED_BY;
  }

  @Override
  public Instant getAddedAt() {
    return addedAt;
  }
}
