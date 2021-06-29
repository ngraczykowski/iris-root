package com.silenteight.serp.governance.qa;

import com.silenteight.serp.governance.qa.manage.domain.DecisionLevel;
import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import java.time.OffsetDateTime;

import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.VALIDATION;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.FAILED;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.NEW;
import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public class DecisionFixture {

  public static final DecisionState STATE_NEW = NEW;
  public static final DecisionState STATE_FAILED = FAILED;
  public static final DecisionLevel LEVEL_ANALYSIS = ANALYSIS;
  public static final DecisionLevel LEVEL_VALIDATION = VALIDATION;
  public static final String COMMENT_OK = "OK";
  public static final String COMMENT_FAILED = "FAILED";
  public static final String DECIDED_BY = "username-analysis";
  public static final String DECIDED_AT_FORMAT = "2020-05-21T01:01:01+01:00";
  public static final OffsetDateTime DECIDED_AT = parse(DECIDED_AT_FORMAT, ISO_OFFSET_DATE_TIME);
}
