package com.silenteight.hsbc.datasource.feature.country;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@RequiredArgsConstructor
@Getter
enum InvalidCountryPattern {
  XX_PATTERN(compile("\\bXX\\b", Pattern.CASE_INSENSITIVE)),
  ZZ_PATTERN(compile("\\bZZ\\b", Pattern.CASE_INSENSITIVE)),
  LN_PATTERN(compile("\\bLN\\b", Pattern.CASE_INSENSITIVE)),
  UNK_PATTERN(compile("\\bUNK\\b", Pattern.CASE_INSENSITIVE)),
  UNKNOWN_PATTERN(compile("\\bUNKNOWN\\b", Pattern.CASE_INSENSITIVE)),
  UNSPECIFIED_PATTERN(compile("\\bUNSPECIFIED\\b", Pattern.CASE_INSENSITIVE)),
  NULL_PATTERN(compile("\\bNULL\\b", Pattern.CASE_INSENSITIVE)),
  DEFAULT_PATTERN(compile("\\bDEFAULT\\b", Pattern.CASE_INSENSITIVE));


  private final Pattern pattern;
}
