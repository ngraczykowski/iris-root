package com.silenteight.warehouse.indexer.query.streaming;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class FieldDefinition {

  @NonNull
  String name;
  @NonNull
  String label;
}
