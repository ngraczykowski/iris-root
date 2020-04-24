package com.silenteight.sens.webapp.user.sync.analyst.domain;

import com.silenteight.sens.webapp.user.domain.ExternalOrigin;

public class GnsOrigin extends ExternalOrigin {

  public static final String GNS_ORIGIN = "GNS";

  @Override
  public String toString() {
    return GNS_ORIGIN;
  }
}
