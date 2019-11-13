package com.silenteight.sens.webapp.backend.rest.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ControllerAdviceOrder {

  static final int DECISION_TREE = 0;
  static final int BRANCH = 1;
  static final int USERS = 2;
  static final int WORKFLOW = 3;
  static final int RESTRICTION = 4;
  static final int GLOBAL = Integer.MAX_VALUE - 1;
  static final int UNKNOWN = Integer.MAX_VALUE;
}
