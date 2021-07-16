package com.silenteight.serp.governance.qa.manage.analysis;

import com.silenteight.serp.governance.qa.manage.analysis.list.dto.AlertAnalysisDto;
import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import java.time.Instant;

import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_CREATED_AT;
import static com.silenteight.serp.governance.qa.AlertFixture.DISCRIMINATOR;
import static com.silenteight.serp.governance.qa.DecisionFixture.COMMENT_OK;
import static com.silenteight.serp.governance.qa.DecisionFixture.DECIDED_AT;
import static com.silenteight.serp.governance.qa.DecisionFixture.DECIDED_BY;
import static com.silenteight.serp.governance.qa.DecisionFixture.STATE_FAILED;

public class DummyAlertAnalysisDto implements AlertAnalysisDto {

  @Override
  public String getDiscriminator() {
    return DISCRIMINATOR;
  }

  @Override
  public DecisionState getState() {
    return STATE_FAILED;
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
  public String getDecisionComment() {
    return COMMENT_OK;
  }

  @Override
  public Instant getAddedAt() {
    return ALERT_CREATED_AT.toInstant();
  }
}
