package com.silenteight.warehouse.indexer.production.indextracking;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.data.api.v1.Alert;

@Value
public class AlertWithIndex {

  @NonNull
  Alert alert;
  @NonNull
  String indexName;
}
