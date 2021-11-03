package com.silenteight.hsbc.datasource.feature.country;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.join;

class CountryNormalizer {

  private final static Pattern WHITE_SPACE_PATTERN = compile("[\\s]+", Pattern.CASE_INSENSITIVE);

  static String normalize(String country, List<Pattern> cleanPatterns) {
    var normalized = country;
    for (var pattern : cleanPatterns) {
      normalized = pattern.matcher(normalized).replaceAll("");
    }
    return WHITE_SPACE_PATTERN.matcher(normalized).replaceAll(" ").strip().toUpperCase();
  }
}
