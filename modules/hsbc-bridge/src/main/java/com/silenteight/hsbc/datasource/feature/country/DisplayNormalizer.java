package com.silenteight.hsbc.datasource.feature.country;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;

class DisplayNormalizer {

  private final Pattern NON_WORD_AND_WHITESPACE_AND_OTHER_CHARACTERS = compile("[^\\w\\s,()\\-'.]", Pattern.CASE_INSENSITIVE);

  private final List<Pattern> cleanPatterns;

  DisplayNormalizer() {
    this.cleanPatterns = Stream.of(InvalidCountryPattern.values()).map(InvalidCountryPattern::getPattern).collect(Collectors.toList());
    cleanPatterns.addAll(List.of(NON_WORD_AND_WHITESPACE_AND_OTHER_CHARACTERS));
  }

  String normalize(String country) {
    return CountryNormalizer.normalize(country, cleanPatterns);
  }
}
