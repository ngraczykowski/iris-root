package com.silenteight.warehouse.indexer.query.common;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class QueryFilter {

  @NonNull
  String field;

  @NonNull
  List<String> allowedValues;
}
