package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@Value
@Builder
public class RegisteredAlert {

  @NonNull
  String batchId;
  @NonNull
  String alertId;
  @NonNull
  String alertName;
  AlertStatus status;
  AlertErrorDescription errorDescription;
  ParsedMessageData parsedMessageData;
  List<Match> matches;

  @Value
  @Builder
  public static class Match {

    @NonNull
    String matchId;
    @NonNull
    String matchName;
    JsonNode payload;
  }
}
