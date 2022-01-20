package com.silenteight.warehouse.indexer.production.qa.indextracking;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.data.api.v2.QaAlert;

@Value
@Builder
public class QaAlertWithIndex {
  @NonNull
  QaAlert alert;
  @NonNull
  String indexName;
  @NonNull
  String discriminator;
}
