package com.silenteight.serp.governance.qa.validation;

import com.silenteight.serp.governance.qa.domain.DecisionState;
import com.silenteight.serp.governance.qa.validation.list.dto.AlertValidationDto;

import java.time.Instant;

import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_CREATED_AT;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.DecisionFixture.COMMENT_OK;
import static com.silenteight.serp.governance.qa.DecisionFixture.DECIDED_AT;
import static com.silenteight.serp.governance.qa.DecisionFixture.DECIDED_BY;
import static com.silenteight.serp.governance.qa.DecisionFixture.STATE_FAILED;

public class DummyAlertValidationDto implements AlertValidationDto {

  @Override
  public String getAlertName() {
    return ALERT_NAME;
  }

  @Override
  public DecisionState getState() {
    return STATE_FAILED;
  }

  @Override
  public String getDecisionComment() {
    return COMMENT_OK;
  }

  @Override
  public Instant getDecisionAt() {
    return DECIDED_AT.toInstant();
  }

  @Override
  public String getDecisionBy() {
    return DECIDED_BY;
  }

  @Override
  public Instant getAddedAt() {
    return ALERT_CREATED_AT.toInstant();
  }
}
