package com.silenteight.warehouse.indexer.analysis;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnalysisMetadataDto {

  String elasticIndexName;
}
