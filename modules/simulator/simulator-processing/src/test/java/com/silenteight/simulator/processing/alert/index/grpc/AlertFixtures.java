package com.silenteight.simulator.processing.alert.index.grpc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AlertFixtures {

  static final String NAME = "alerts/a9b45451-6fde-4832-8dc0-d17b4708d8ca";
  static final String ID = "12345678";
  static final int PRIORITY = 1;
  static final Map<String, String> LABELS = Map.of("country", "SG", "riskType", "PEP");

  static final Alert ALERT =
      Alert.newBuilder()
          .setName(NAME)
          .setAlertId(ID)
          .setPriority(PRIORITY)
          .putAllLabels(LABELS)
          .build();
}
