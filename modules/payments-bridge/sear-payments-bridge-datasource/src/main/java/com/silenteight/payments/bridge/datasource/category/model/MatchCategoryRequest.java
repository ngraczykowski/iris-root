package com.silenteight.payments.bridge.datasource.category.model;

import lombok.Value;

@Value
public class MatchCategoryRequest {

  String match;

  String category;
}
