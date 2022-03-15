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
  String batchName;
  @NonNull
  String messageName;
  @NonNull
  String alertName;
  AlertStatus status;
  AlertErrorDescription errorDescription;
  ParsedMessageData parsedMessageData;
  String systemId;
  List<Match> matches;

  @Value
  @Builder
  public static class Match {

    @NonNull
    String hitName;
    @NonNull
    String matchName;
    JsonNode payload;
  }
}
