package com.silenteight.hsbc.datasource.feature.date;

import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RequiredArgsConstructor
class DateDeviationsFilter implements Predicate<String> {

  private static final Predicate<String> FILTERED_PATTERNS_PREDICATE = Stream.of(
          Pattern.compile("^0$"),
          Pattern.compile("^(00:)*00\\.0$"),
          Pattern.compile("^9999.*$"),
          Pattern.compile("^9999-12-31$"),
          Pattern.compile("^11111111$"),
          Pattern.compile("^11971031$"),
          Pattern.compile("^1901-01-01$")
      )
      .map(Pattern::asMatchPredicate)
      .reduce(Predicate::or)
      .get();

  @Override
  public boolean test(String date) {
    return FILTERED_PATTERNS_PREDICATE.negate().test(date);
  }
}
