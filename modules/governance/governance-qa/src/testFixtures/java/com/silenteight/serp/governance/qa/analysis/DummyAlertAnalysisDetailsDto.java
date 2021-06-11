package com.silenteight.serp.governance.qa.analysis;

import com.silenteight.serp.governance.qa.analysis.details.dto.AlertAnalysisDetailsDto;
import com.silenteight.serp.governance.qa.domain.DecisionState;

import java.time.OffsetDateTime;

import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_CREATED_AT;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static com.silenteight.serp.governance.qa.DecisionFixture.COMMENT_OK;
import static com.silenteight.serp.governance.qa.DecisionFixture.DECIDED_AT;
import static com.silenteight.serp.governance.qa.DecisionFixture.DECIDED_BY;
import static com.silenteight.serp.governance.qa.DecisionFixture.STATE_FAILED;

public class DummyAlertAnalysisDetailsDto implements AlertAnalysisDetailsDto {

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
