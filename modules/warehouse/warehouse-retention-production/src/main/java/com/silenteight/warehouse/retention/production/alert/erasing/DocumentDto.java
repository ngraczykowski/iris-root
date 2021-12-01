package com.silenteight.warehouse.retention.production.alert.erasing;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class DocumentDto {

  @NonNull
  String indexName;
  @NonNull
  String documentId;
}
