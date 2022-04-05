package com.silenteight.connector.ftcc.request.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RequestFixtures {

  static final UUID BATCH_ID = fromString("558ecea2-a1d5-11eb-bcbc-0242ac130002");
  static final UUID MESSAGE_ID = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
  static final String PAYLOAD = "{\"Message\":{"
      + "\"Unit\":\"TRAINING\","
      + "\"BusinessUnit\":\"\","
      + "\"MessageID\":\"00000004\","
      + "\"SystemID\":\"TRAINING!60C2ED1B-58A1D68E-0326AE78-A8C7CC79\","
      + "\"CurrentStatus\":{"
      + "\"ID\":\"0\","
      + "\"Name\":\"NEW\","
      + "\"RoutingCode\":\"0\","
      + "\"Checksum\":\"ed10e9af8ecdee8e303f4c95783757c6\""
      + "}}}";
}
