package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AlertWithMatches {

  String alertId;
  AlertStatus status;
  AlertMetadata metadata;
  AlertErrorDescription alertErrorDescription;

  @Builder.Default
  List<Match> matches = List.of();
}
