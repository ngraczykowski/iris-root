/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.domain.category;

import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DenyCategoryParser {

  public static String isDenyYesNo(@Nullable String unit) {
    return isDeny(unit) ? "YES" : "NO";
  }

  public static boolean isDeny(@Nullable String unit) {
    return nullToEmpty(unit).contains("DENY");
  }
}
