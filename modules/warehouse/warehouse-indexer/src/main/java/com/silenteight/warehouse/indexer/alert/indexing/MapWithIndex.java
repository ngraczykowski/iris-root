package com.silenteight.warehouse.indexer.alert.indexing;

import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
public class MapWithIndex {

  @NonNull
  String indexName;
  @NonNull
  String documentId;
  @NonNull
  Map<String, Object> payload;
}
