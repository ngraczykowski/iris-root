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
  @NonNull
  ParsedMessageData parsedMessageData;
  String systemId;
  String messageId;
  String currentStatusName;
  String currentActionDateTime;
  String currentActionComment;
  Map<String, Hit> hits;

  public Hit getHit(String hitName) {
    return hits.get(hitName);
  }

  public String getDiscriminator() {
    return systemId + "|" + messageId;
  }

  @Value
  @Builder
  public static class Hit {

    @NonNull
    String hitName;

    /**
     * Payloads with same ofacId
     */
    @NonNull
    List<JsonNode> payloads;
  }
}
