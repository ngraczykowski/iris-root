package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@Builder
public class RegisteredAlert {

  @NonNull
  String batchName;
  @NonNull
  String alertName;
  @NonNull
  String messageName;
  @NonNull
  @Builder.Default
  AlertStatus status = AlertStatus.SUCCESS;
  AlertErrorDescription errorDescription;
  ParsedMessageData parsedMessageData;
  String systemId;
  @Builder.Default
  List<Match> matches = emptyList();

  @Value
  @Builder
  public static class Match {

    @NonNull
    String matchName;
    List<JsonNode> payloads;
  }
}
