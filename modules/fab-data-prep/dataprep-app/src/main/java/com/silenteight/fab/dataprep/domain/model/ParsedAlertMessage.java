package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class ParsedAlertMessage {

  @NonNull
  String batchName;
  @NonNull
  String messageName;
  ParsedMessageData parsedMessageData;
  String systemId;
  String messageId;
  Map<String, Hit> hits;

  public Hit getHit(String hitName) {
    return hits.get(hitName);
  }

  @Value
  @Builder
  public static class Hit {

    String hitName;

    /**
     * Payloads with same ofacId
     */
    List<JsonNode> payloads;
  }
}
