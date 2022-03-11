package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RegistrationRequest {

  String batchId;

  @Builder.Default
  List<AlertWithMatchesMetadata> alertsWithMatches = List.of();

  public static RegistrationRequest of(
      Batch batch, List<AlertWithMatchesMetadata> alertsWithMatches) {
    return RegistrationRequest.builder()
        .batchId(batch.id())
        .alertsWithMatches(alertsWithMatches)
        .build();
  }
}
