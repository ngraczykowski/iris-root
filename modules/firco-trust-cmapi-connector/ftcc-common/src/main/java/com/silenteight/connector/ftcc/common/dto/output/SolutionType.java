package com.silenteight.connector.ftcc.common.dto.output;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SolutionType {

  ERROR("-1"),
  UNKNOWN("0"),
  NAME("1"),
  SEARCH_CODE("2"),
  PASSPORT("3"),
  NATIONAL_ID("4"),
  BIC("5"),
  EMBARGO("6"),
  FML_RULE("7");

  @Getter
  private final String code;
}
