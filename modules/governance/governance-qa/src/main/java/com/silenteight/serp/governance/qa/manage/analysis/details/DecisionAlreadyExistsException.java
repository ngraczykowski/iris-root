package com.silenteight.serp.governance.qa.manage.analysis.details;

import com.silenteight.serp.governance.qa.manage.domain.DecisionLevel;

import static java.lang.String.format;

public class DecisionAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = -4053597311211083842L;

  public DecisionAlreadyExistsException(String discriminator, DecisionLevel level) {
    super(format("Decision for alert discriminator=%s on level=%d already exists.", discriminator,
        level.getValue()));
  }
}
