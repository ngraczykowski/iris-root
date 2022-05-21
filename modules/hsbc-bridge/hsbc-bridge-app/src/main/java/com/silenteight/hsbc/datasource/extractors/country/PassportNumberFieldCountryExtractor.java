package com.silenteight.hsbc.datasource.extractors.country;

import com.silenteight.hsbc.datasource.extractors.common.SimpleRegexBasedExtractor;

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
