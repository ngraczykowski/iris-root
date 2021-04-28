package com.silenteight.serp.governance.model.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder
public class ModelDto {

  @NonNull
  String name;
  @NonNull
  String policy;
  @NonNull
  OffsetDateTime createdAt;
}
