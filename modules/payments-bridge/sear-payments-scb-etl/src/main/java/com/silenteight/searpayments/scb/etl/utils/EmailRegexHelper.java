package com.silenteight.searpayments.scb.etl.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Return true if find email pattern else false
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailRegexHelper {

  private static final String EMAIL_REGEX = "(\\w\\.\\w)";

  private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

  static boolean checkEmail(String text) {
    if (null == text)
      return false;

    Matcher matcher = PATTERN.matcher(text);
    return matcher.find();
  }
}
