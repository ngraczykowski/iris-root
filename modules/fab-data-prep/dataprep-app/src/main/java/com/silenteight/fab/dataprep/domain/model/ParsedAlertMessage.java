package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

@Value
@Builder
public class ParsedAlertMessage {

  @NonNull
  String batchName;
  @NonNull
  String messageName;
  AlertStatus status;
  AlertErrorDescription errorDescription;
  ParsedMessageData parsedMessageData;
  String systemId;
  Map<String, Hit> hits;

  public Hit getHit(String hitName) {
    return hits.get(hitName);
  }

  @Value
  @Builder
  public static class Hit {

    String hitName;
    JsonNode payload;
  }
}
