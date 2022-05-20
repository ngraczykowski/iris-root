package com.silenteight.warehouse.common.web.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControllerAdviceOrder {

  public static final int GLOBAL = Integer.MAX_VALUE - 1;
  public static final int UNKNOWN = Integer.MAX_VALUE;
}
