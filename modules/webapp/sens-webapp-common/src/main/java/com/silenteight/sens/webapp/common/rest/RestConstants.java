package com.silenteight.sens.webapp.common.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestConstants {

  public static final String ROOT = "/api";
  public static final String MANAGEMENT_PREFIX = "/management";
  public static final String LOGOUT_URL = "/logout";
  public static final String API_LOGOUT_URL = ROOT + LOGOUT_URL;
  public static final String AUDIT_TRAIL_PREFIX = "/audit-trail";
  public static final String DECISION_TREES_PREFIX = "/decision-trees";
  public static final String DECISION_TREE_PREFIX = "/decision-tree";
}
