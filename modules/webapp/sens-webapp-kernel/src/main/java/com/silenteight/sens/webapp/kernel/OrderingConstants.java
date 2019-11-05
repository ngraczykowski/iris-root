package com.silenteight.sens.webapp.kernel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.core.Ordered;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderingConstants {

  public static final int GLOBAL_METHOD_SECURITY_ORDER = Ordered.LOWEST_PRECEDENCE - 1;

  public static final int TRANSACTIONAL_MANAGEMENT_ORDER = GLOBAL_METHOD_SECURITY_ORDER - 1;
}
