package com.silenteight.hsbc.datasource.extractors.country;

import com.silenteight.hsbc.datasource.extractors.common.SimpleRegexBasedExtractor;

import java.util.Optional;
import java.util.regex.Pattern;

public class NationalIdNumberFieldCountryExtractor extends SimpleRegexBasedExtractor {

  private static final Pattern EXTRACTION_REGEX = Pattern.compile(".+\\((.*)\\)");

  public NationalIdNumberFieldCountryExtractor(String fieldValue) {
    super(fieldValue);
  }

  @Override
  public Optional<String> extract() {
    return super.extract()
        .map(s -> s.replace("-", " "));
  }

  @Override
  public Pattern getPattern() {
    return EXTRACTION_REGEX;
  }
}
