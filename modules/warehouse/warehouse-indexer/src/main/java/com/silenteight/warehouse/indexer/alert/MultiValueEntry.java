package com.silenteight.warehouse.indexer.alert;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class MultiValueEntry {

  @NonNull
  String field;
  @NonNull
  List<String> values;
}
