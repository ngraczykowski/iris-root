package com.silenteight.serp.governance.common.web.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControllerAdviceOrder {

  public static final int RESTRICTION = 4;
  public static final int REPORT = 5;

  public static final int GLOBAL = Integer.MAX_VALUE - 1;
  public static final int UNKNOWN = Integer.MAX_VALUE;
}
