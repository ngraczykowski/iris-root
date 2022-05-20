package com.silenteight.hsbc.bridge.util.environment;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
class SensitivePropertiesMatcher {

  private static final List<Pattern> DEFAULT_PATTERNS = Arrays.asList(
      caseInsensitive(".*creden.*"),
      caseInsensitive(".*pass.*"),
      caseInsensitive(".*crypt.*"),
      caseInsensitive(".*secret.*"),
      caseInsensitive(".*key[0-9]*")
  );

  private final List<Pattern> patterns;

  SensitivePropertiesMatcher() {
    this(DEFAULT_PATTERNS);
  }

  boolean matches(String prop) {
    return patterns.stream().anyMatch(p -> p.matcher(prop).matches());
  }

  private static Pattern caseInsensitive(String pattern) {
    return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
  }
}
