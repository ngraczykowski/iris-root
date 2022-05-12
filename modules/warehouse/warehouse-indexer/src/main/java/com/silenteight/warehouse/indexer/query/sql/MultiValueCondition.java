package com.silenteight.warehouse.indexer.query.sql;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

import static java.util.Arrays.asList;

@Value
@RequiredArgsConstructor
public class MultiValueCondition {

  @NonNull
  String field;
  @NonNull
  List<String> values;

  public MultiValueCondition(String field, String... values) {
    this.field = field;
    this.values = asList(values);
  }
}
