package com.silenteight.hsbc.datasource.feature.date;

import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class DateDeviationsFilter implements Predicate<String> {

  private static final Predicate<String> FILTERED_PATTERNS_PREDICATE = of(
      compile("^0$"),
      compile("^(00:)*00\\.0$"),
      compile("^9999.*$"),
      compile("^9999-12-31$"),
      compile("^11111111$"),
      compile("^11971031$"),
      compile("^1901-01-01$")
  )
      .map(Pattern::asMatchPredicate)
      .reduce(Predicate::or)
      .get();

  @Override
  public boolean test(String date) {
    return FILTERED_PATTERNS_PREDICATE.negate().test(date);
  }
}
