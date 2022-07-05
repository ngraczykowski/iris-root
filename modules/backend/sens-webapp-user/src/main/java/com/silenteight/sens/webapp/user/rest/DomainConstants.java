/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.sens.webapp.user.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainConstants {

  public static final int USER_FIELD_MIN_LENGTH = 3;
  public static final int USER_FIELD_MAX_LENGTH = 64;
}
