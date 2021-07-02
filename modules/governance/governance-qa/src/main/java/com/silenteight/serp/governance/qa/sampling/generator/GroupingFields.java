package com.silenteight.serp.governance.qa.sampling.generator;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

enum GroupingFields {

  RISK_TYPE("riskType");

  private final String value;

  GroupingFields(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static List<String> valuesAsStringList() {
    return stream(values())
        .map(GroupingFields::getValue)
        .collect(toList());
  }
}
