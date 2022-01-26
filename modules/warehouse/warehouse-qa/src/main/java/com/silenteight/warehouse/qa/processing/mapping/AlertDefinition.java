package com.silenteight.warehouse.qa.processing.mapping;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class AlertDefinition {

  @NonNull
  private String name;
  @NonNull
  private String payload;
}
