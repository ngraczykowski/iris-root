package com.silenteight.warehouse.indexer.alert.mapping;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.google.protobuf.Struct;

import javax.annotation.Nullable;

@Data
@Builder
public class AlertDefinition {

  @Nullable
  private String discriminator;
  @Nullable
  private String name;
  @NonNull
  private Struct payload;
}
