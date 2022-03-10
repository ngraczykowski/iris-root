package com.silenteight.connector.ftcc.ingest.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.UUID;

import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class MessageFixtures {

  static final UUID BATCH_ID = fromString("558ecea2-a1d5-11eb-bcbc-0242ac130002");
  static final JsonNode PAYLOAD = new TextNode("{\"message\":\"Message\"}");
}
