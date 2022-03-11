package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

@Value
@Builder
public class ExtractedAlert {

  @NonNull
  String batchId;
  @NonNull
  String alertId;
  AlertStatus status;
  AlertErrorDescription errorDescription;
  ParsedMessageData parsedMessageData;
  Map<String, Match> matches;

  public Match getMatch(String matchId) {
    return matches.get(matchId);
  }

  @Value
  @Builder
  public static class Match {

    String matchId;
    JsonNode payload;
  }
}
