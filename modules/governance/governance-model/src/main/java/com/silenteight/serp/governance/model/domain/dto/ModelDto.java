package com.silenteight.serp.governance.model.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Value
@Builder
public class ModelDto {

  public static final String DEFAULT_MODEL_NAME = "models/default";
  public static final UUID DEFAULT_MODEL_ID = randomUUID();

  @NonNull
  UUID id;
  @NonNull
  String name;
  @NonNull
  String policyName;
  @NonNull
  OffsetDateTime createdAt;
}
