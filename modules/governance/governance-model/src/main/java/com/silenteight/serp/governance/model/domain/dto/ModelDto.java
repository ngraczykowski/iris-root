package com.silenteight.serp.governance.model.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ModelDto {

  @NonNull
  UUID id;

  @NonNull
  String name;
}
