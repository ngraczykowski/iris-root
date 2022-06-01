/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.payments.bridge.common.app;

import lombok.NonNull;

public class StringUtil {

  private static final String LEGAL_REGEX_CHARACTERS =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_ ";

  public static String regexEscape(@NonNull String input) {
    var len = input.length();
    StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < len; i++) {
      var c = input.charAt(i);
      if (LEGAL_REGEX_CHARACTERS.indexOf(c) < 0) buffer.append('\\');
      buffer.append(c);
    }

    return buffer.toString();
  }
}
