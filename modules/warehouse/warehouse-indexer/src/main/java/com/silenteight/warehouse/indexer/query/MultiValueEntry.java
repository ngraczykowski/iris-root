package com.silenteight.warehouse.indexer.query;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
// TODO: probably shouldn't have MultiValueEntry as we could use directly ListMultimap
public class MultiValueEntry {

  @NonNull
  String field;
  @NonNull
  List<String> values;
}
