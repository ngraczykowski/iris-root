package com.silenteight.hsbc.datasource.extractors.common;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public abstract class SimpleRegexBasedExtractor {

  private final String fieldValue;

  public Optional<String> extract() {
    var matcher = getPattern().matcher(fieldValue);

    if (matcher.find()) {
      return Optional.of(matcher.group(1));
    }

    return Optional.empty();
  }

  public abstract Pattern getPattern();
}
