package com.silenteight.payments.bridge.datasource.category.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MatchCategoryValue {

  String match;

  String category;

  String value;

}
