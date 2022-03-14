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
  static final UUID MESSAGE_ID = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
  static final JsonNode PAYLOAD = new TextNode("{\"message\":\"Message\"}");
}
