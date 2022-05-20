package com.silenteight.hsbc.datasource.feature.country;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
enum InvalidCountryPattern {
  XX_PATTERN(Pattern.compile("\\bXX\\b", Pattern.CASE_INSENSITIVE)),
  ZZ_PATTERN(Pattern.compile("\\bZZ\\b", Pattern.CASE_INSENSITIVE)),
  LN_PATTERN(Pattern.compile("\\bLN\\b", Pattern.CASE_INSENSITIVE)),
  UNK_PATTERN(Pattern.compile("\\bUNK\\b", Pattern.CASE_INSENSITIVE)),
  UNKNOWN_PATTERN(Pattern.compile("\\bUNKNOWN\\b", Pattern.CASE_INSENSITIVE)),
  UNSPECIFIED_PATTERN(Pattern.compile("\\bUNSPECIFIED\\b", Pattern.CASE_INSENSITIVE)),
  NULL_PATTERN(Pattern.compile("\\bNULL\\b", Pattern.CASE_INSENSITIVE)),
  DEFAULT_PATTERN(Pattern.compile("\\bDEFAULT\\b", Pattern.CASE_INSENSITIVE));


  private final Pattern pattern;
}
