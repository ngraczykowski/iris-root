package com.silenteight.warehouse.test.generator;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import static java.util.List.of;
import static java.util.UUID.randomUUID;

class AlertGenerator {

  Alert generate() {
    return Alert.newBuilder()
        .setName(getRandomAlertName())
        .setPayload(structWithValue(
            "recommendation", "FALSE_POSITIVE"))
        .addAllMatches(of(
            match(getRandomMatchName(), "solution", "NO_DECISION")))
        .build();
  }

  static Builder structWithValue(String key, String value) {
    return Struct.newBuilder()
        .putFields(key, Value.newBuilder()
            .setStringValue(value)
            .build());
  }

  private static Match match(String matchName, String payloadName, String payloadSolution) {
    return Match.newBuilder()
        .setName(matchName)
        .setPayload(structWithValue(payloadName, payloadSolution))
        .build();
  }

  private String getRandomAlertName() {
    return "alerts/" + randomUUID().toString();
  }

  private String getRandomMatchName() {
    return "matches/" + randomUUID().toString();
  }
}
