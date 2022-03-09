package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@Value
@Builder
public class ExtractedAlert {

  @NonNull
  String batchId;
  @NonNull
  String alertId;
  String alertName;
  AlertStatus status;
  AlertErrorDescription errorDescription;
  ParsedPayload parsedPayload;
  List<Match> matches;

  @Value
  @Builder
  public static class Match {

    String matchId;
    String matchName;
    JsonNode payload;
  }
}
