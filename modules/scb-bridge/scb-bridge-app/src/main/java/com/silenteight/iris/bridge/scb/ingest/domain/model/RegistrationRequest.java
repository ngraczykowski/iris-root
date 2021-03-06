/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RegistrationRequest {

  String batchId;

  @Builder.Default
  List<AlertWithMatches> alertsWithMatches = List.of();

  public static RegistrationRequest of(
      String batchId, List<AlertWithMatches> alertsWithMatches) {
    return RegistrationRequest.builder()
        .batchId(batchId)
        .alertsWithMatches(alertsWithMatches)
        .build();
  }
}
