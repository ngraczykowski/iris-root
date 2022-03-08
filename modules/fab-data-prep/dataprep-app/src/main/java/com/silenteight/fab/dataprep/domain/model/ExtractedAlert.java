package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ExtractedAlert {

  String batchId;
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
  }
}
