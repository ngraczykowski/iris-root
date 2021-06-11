package com.silenteight.serp.governance.qa.validation;

import com.silenteight.serp.governance.qa.domain.DecisionState;
import com.silenteight.serp.governance.qa.validation.details.dto.AlertValidationDetailsDto;

import java.time.OffsetDateTime;

import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_CREATED_AT;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.DecisionFixture.COMMENT_OK;
import static com.silenteight.serp.governance.qa.DecisionFixture.DECIDED_AT;
import static com.silenteight.serp.governance.qa.DecisionFixture.DECIDED_BY;
import static com.silenteight.serp.governance.qa.DecisionFixture.STATE_FAILED;

public class DummyAlertValidationDetailsDto implements AlertValidationDetailsDto {

  @Override
  public String getAlertName() {
    return ALERT_NAME;
  }

  @Override
  public DecisionState getState() {
    return STATE_FAILED;
  }

  @Override
  public OffsetDateTime getDecisionAt() {
    return DECIDED_AT;
  }

  @Override
  public String getDecisionBy() {
    return DECIDED_BY;
  }

  @Override
  public String getDecisionComment() {
    return COMMENT_OK;
  }

  @Override
  public OffsetDateTime getAddedAt() {
    return ALERT_CREATED_AT;
  }
}
