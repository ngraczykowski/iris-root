package com.silenteight.hsbc.datasource.feature.converter.country;

import com.silenteight.hsbc.datasource.feature.converter.SimpleRegexBasedExtractor;

import java.util.regex.Pattern;

public class PassportNumberFieldCountryExtractor extends SimpleRegexBasedExtractor {

  private static final Pattern EXTRACTION_REGEX = Pattern.compile(".+\\((.*)\\)");

  public PassportNumberFieldCountryExtractor(String fieldValue) {
    super(fieldValue);
  }

  @Override
  public Pattern getPattern() {
    return EXTRACTION_REGEX;
  }
}
