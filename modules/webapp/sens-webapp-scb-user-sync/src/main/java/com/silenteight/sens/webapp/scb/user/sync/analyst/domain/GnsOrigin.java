package com.silenteight.sens.webapp.scb.user.sync.analyst.domain;


import com.silenteight.sep.usermanagement.api.origin.UserOrigin;

public class GnsOrigin implements UserOrigin {

  public static final String GNS_ORIGIN = "GNS";

  @Override
  public String toString() {
    return GNS_ORIGIN;
  }
}
