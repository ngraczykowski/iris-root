package com.silenteight.sens.webapp.common.support.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WhitespaceUtils {

  private static final Pattern NBSP_PATTERN = Pattern.compile("\\h");

  public static String normalizeNbspChars(String text) {
    return NBSP_PATTERN.matcher(text).replaceAll(" ");
  }
}
