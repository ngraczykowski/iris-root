package com.silenteight.warehouse.backup.storage;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import static java.util.List.of;

public class ProductionDataIndexRequestFixtures {

  public static final String ANALYSIS_NAME = "analysis/24dbf320-57dd-43fe-85a0-101f6f33d81a";
  public static final String DISCRIMINATOR = "457b1498-e348-4a81-8093-6079c1173010";
  public static final String REQUEST_ID = "d25641e9-71d6-4705-a90b-5bc3d5303425";
  public static final String RISK_TYPE = "riskType";
  public static final String SANCTION = "SAN";
  static final Struct PAYLOAD_WITH_SANCTION = Struct.newBuilder()
      .putFields(RISK_TYPE, getValue(SANCTION))
      .build();
  static final Alert ALERT = Alert.newBuilder()
      .setDiscriminator(DISCRIMINATOR)
      .setPayload(PAYLOAD_WITH_SANCTION)
      .build();

  static final ProductionDataIndexRequest PRODUCTION_DATA_INDEX_REQUEST_1 =
      ProductionDataIndexRequest.newBuilder()
          .setAnalysisName(ANALYSIS_NAME)
          .setRequestId(REQUEST_ID)
          .addAllAlerts(of(ALERT))
          .build();

  static final String PRODUCTION_DATA_JSON_FORMAT = ""
      + "{\"alerts\": [{\"payload\": {\"riskType\": \"SAN\"}, "
      + "\"discriminator\": \"457b1498-e348-4a81-8093-6079c1173010\"}], "
      + "\"requestId\": \"d25641e9-71d6-4705-a90b-5bc3d5303425\", "
      + "\"analysisName\": \"analysis/24dbf320-57dd-43fe-85a0-101f6f33d81a\"}";

  private static Value getValue(String value) {
    return Value.newBuilder()
        .setStringValue(value)
        .build();
  }
}
