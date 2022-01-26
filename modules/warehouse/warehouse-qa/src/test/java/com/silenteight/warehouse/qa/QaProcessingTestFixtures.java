package com.silenteight.warehouse.qa;

import com.silenteight.data.api.v2.QaAlert;
import com.silenteight.data.api.v2.QaAlert.State;
import com.silenteight.data.api.v2.QaDataIndexRequest;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.Map;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;

public class QaProcessingTestFixtures {

  public static final String PROCESSING_TIMESTAMP = "2021-04-15T12:17:37.098Z";
  static final String ALERT_DISCRIMINATOR_1 = "faa4f5c7-07ec-48d8-b4d5-e25b432ea78f";

  static final String QA_REQUEST_ID = "ffd16a59-5523-473f-b342-e66af40a28a6";
  static final String QA_COMMENT = "Test comment";
  static final String PRODUCTION_REQUEST_ID = "aad16a59-5523-473f-b342-e66af40a28a6";
  static final String ALERT_NAME = "alerts/123";

  static final Struct PAYLOAD_1 = Struct.newBuilder()
      .putAllFields(Map.of(
          "name", getValue(ALERT_NAME)))
      .build();


  static final com.silenteight.data.api.v1.Alert ALERT_V1 =
      com.silenteight.data.api.v1.Alert.newBuilder()
          .setDiscriminator(ALERT_DISCRIMINATOR_1)
          .setPayload(PAYLOAD_1)
          .setName(ALERT_NAME)
          .build();

  static final com.silenteight.data.api.v1.ProductionDataIndexRequest PRODUCTION_REQUEST_V1 =
      com.silenteight.data.api.v1.ProductionDataIndexRequest.newBuilder()
          .addAllAlerts(of(ALERT_V1))
          .setRequestId(PRODUCTION_REQUEST_ID)
          .build();

  static final QaAlert QA_ALERT = QaAlert.newBuilder()
      .setName(ALERT_NAME)
      .setLevel(0)
      .setState(State.NEW)
      .setComment(QA_COMMENT)
      .setTimestamp(toTimestamp(parse(PROCESSING_TIMESTAMP)))
      .build();

  static final QaDataIndexRequest QA_REQUEST = QaDataIndexRequest.newBuilder()
      .setRequestId(QA_REQUEST_ID)
      .addAlerts(QA_ALERT)
      .build();

  private static Value getValue(String value) {
    return Value.newBuilder()
        .setStringValue(value)
        .build();
  }
}
