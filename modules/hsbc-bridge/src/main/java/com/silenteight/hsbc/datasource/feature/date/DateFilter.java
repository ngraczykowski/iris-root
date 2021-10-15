package com.silenteight.hsbc.datasource.feature.date;

import java.util.function.Predicate;
import java.util.regex.Pattern;

class DateFilter implements Predicate<String> {

  private static final Predicate<String> DATE_PATTERN_PREDICATE =
      Pattern.compile("^.*[0-9]+.*$").asMatchPredicate();

  @Override
  public boolean test(String date) {
    return DATE_PATTERN_PREDICATE.test(date);
  }
}
