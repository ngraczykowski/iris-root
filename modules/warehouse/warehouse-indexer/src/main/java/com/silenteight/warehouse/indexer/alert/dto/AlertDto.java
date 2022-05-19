package com.silenteight.warehouse.indexer.alert.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.Map;

@Value
@Builder
public class AlertDto {

  @NonNull
  Long id;

  @NonNull
  String discriminator;

  String name;

  @NonNull
  Instant createdAt;

  @NonNull
  Instant recommendationDate;

  // This is a json consider other type to store it in better way
  @NonNull
  Map<String, String> payload;
}
