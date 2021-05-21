package com.silenteight.warehouse.indexer.analysis;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class AnalysisMetadataDto {

  @NonNull
  String elasticIndexName;
  @NonNull
  String tenant;
}
