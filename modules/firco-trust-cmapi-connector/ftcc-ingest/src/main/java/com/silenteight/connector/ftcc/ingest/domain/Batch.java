package com.silenteight.connector.ftcc.ingest.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Batch {

  @NonNull
  String batchId;
  long alertsCount;
}
