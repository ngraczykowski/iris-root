package com.silenteight.warehouse.indexer.production.qa.indextracking;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class IndexDiscriminatorDto {
  @NonNull
  String indexName;
  @NonNull
  String discriminator;
}
