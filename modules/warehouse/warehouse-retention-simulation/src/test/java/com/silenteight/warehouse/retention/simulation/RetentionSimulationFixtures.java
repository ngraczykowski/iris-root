package com.silenteight.warehouse.retention.simulation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.dataretention.api.v1.AnalysisExpired;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.Map;

import static java.util.List.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RetentionSimulationFixtures {

  static final String ALERT_NAME_1 = "alerts/457b1498-e348-4a81-8093-6079c1173010";
  static final String RISK_TYPE_KEY = "riskType";
  static final String RISK_TYPE_VALUE = "SAN";
  static final String ANALYSIS_NAME = "analysis/120bb00c-8499-4a20-9f1e-e731ea67cbb4";
  static final String REQUEST_ID = "7a71928e-2cdb-4fde-a8ed-5cef6d709636";

  static final Struct PAYLOAD_1 = Struct.newBuilder()
      .putAllFields(Map.of(RISK_TYPE_KEY, toValue(RISK_TYPE_VALUE)))
      .build();

  static final com.silenteight.data.api.v1.Alert ALERT_SIM_1 =
      com.silenteight.data.api.v1.Alert.newBuilder()
          .setName(ALERT_NAME_1)
          .setPayload(PAYLOAD_1)
          .build();

  static final SimulationDataIndexRequest SIMULATION_DATA_INDEX_REQUEST =
      SimulationDataIndexRequest.newBuilder()
          .setRequestId(REQUEST_ID)
          .setAnalysisName(ANALYSIS_NAME)
          .addAlerts(ALERT_SIM_1)
          .build();

  static final AnalysisExpired ANALYSIS_EXPIRED_REQUEST =
      AnalysisExpired.newBuilder()
          .addAllAnalysis(of(ANALYSIS_NAME))
          .build();

  private static Value toValue(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }
}
