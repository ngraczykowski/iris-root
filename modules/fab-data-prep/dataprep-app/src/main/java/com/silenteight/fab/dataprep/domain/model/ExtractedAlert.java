package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@Data
@Builder
public class ExtractedAlert {

  @NonNull
  String batchId;
  @NonNull
  String alertId;
  String alertName;
  AlertStatus status;
  AlertErrorDescription errorDescription;
  ParsedMessageData parsedMessageData;
  List<Match> matches;

  @Data
  @Builder
  public static class Match {

    String matchId;
    String matchName;
    JsonNode payload;
  }
}
