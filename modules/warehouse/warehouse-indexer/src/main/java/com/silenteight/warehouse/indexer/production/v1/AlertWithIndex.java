package com.silenteight.warehouse.indexer.production.v1;

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
