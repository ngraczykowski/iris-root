package com.silenteight.hsbc.datasource.feature.country;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;

class MatchNormalizer {

  private final Pattern THE_PATTERN = compile("\\bthe\\b", Pattern.CASE_INSENSITIVE);
  private final Pattern OF_PATTERN = compile("\\bof\\b", Pattern.CASE_INSENSITIVE);
  private final Pattern NON_WORD_AND_WHITESPACE_CHARACTERS = compile("[^\\w\\s]", Pattern.CASE_INSENSITIVE);

  private final List<Pattern> cleanPatterns;

  MatchNormalizer() {
    this.cleanPatterns = Stream.concat(
        Stream.of(THE_PATTERN, OF_PATTERN, NON_WORD_AND_WHITESPACE_CHARACTERS),
        Stream.of(InvalidCountryPattern.values()).map(InvalidCountryPattern::getPattern)
    ).collect(Collectors.toList());
  }

  String normalize(String country) {
    return CountryNormalizer.normalize(country, cleanPatterns);
  }
}
