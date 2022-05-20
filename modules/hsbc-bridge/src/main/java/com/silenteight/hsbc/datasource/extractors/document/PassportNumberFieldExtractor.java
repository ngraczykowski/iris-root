package com.silenteight.hsbc.datasource.extractors.document;

import com.silenteight.hsbc.datasource.extractors.common.SimpleRegexBasedExtractor;

import java.util.regex.Pattern;

class PassportNumberFieldExtractor extends SimpleRegexBasedExtractor {

  private static final Pattern EXTRACTION_REGEX = Pattern.compile("^(.*) \\((.*)\\)$");

  PassportNumberFieldExtractor(String fieldValue) {
    super(fieldValue);
  }

  @Override
  public Pattern getPattern() {
    return EXTRACTION_REGEX;
  }
}
