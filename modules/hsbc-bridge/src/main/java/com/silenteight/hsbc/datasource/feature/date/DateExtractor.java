package com.silenteight.hsbc.datasource.feature.date;

import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static java.util.List.of;

class DateExtractor implements UnaryOperator<String> {

  // Every pattern needs to have matching group
  private static final List<Pattern> REPLACING_PATTERNS = of(
      Pattern.compile("^(.*) (00:)*00\\.0$"),
      Pattern.compile("^(.*?)(-00)+$"),
      Pattern.compile("^(.*)0000$"),
      Pattern.compile("^(.*)\\.0$")
  );

  @Override
  public String apply(String inputDate) {
    return StreamEx.of(REPLACING_PATTERNS)
        .foldLeft(inputDate, (s, pattern) -> pattern.matcher(s).replaceAll("$1"))
        .trim();
  }
}
