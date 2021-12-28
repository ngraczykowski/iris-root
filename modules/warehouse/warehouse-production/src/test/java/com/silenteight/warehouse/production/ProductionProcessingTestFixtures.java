package com.silenteight.warehouse.production;

import static com.silenteight.warehouse.production.ProductionMessagePersistenceTestFixtures.*;
import static java.util.List.of;

public class ProductionProcessingTestFixtures {

  public static final String PROCESSING_TIMESTAMP = "2021-04-15T12:17:37.098Z";

  static final String REQUEST_ID_V1 = "ffd16a59-5523-473f-b342-e66af40a28a6";

  static final com.silenteight.data.api.v1.Alert ALERT_V1 =
      com.silenteight.data.api.v1.Alert.newBuilder()
          .setDiscriminator(ALERT_DISCRIMINATOR_1)
          .setPayload(PAYLOAD_1)
          .setName(ALERT_NAME_1)
          .build();

  static final com.silenteight.data.api.v1.ProductionDataIndexRequest PRODUCTION_REQUEST_V1 =
      com.silenteight.data.api.v1.ProductionDataIndexRequest.newBuilder()
          .addAllAlerts(of(ALERT_V1))
          .setRequestId(REQUEST_ID_V1)
          .build();

  static final String REQUEST_ID_V2 = "4c8da04e-f7af-4e71-925f-1d8c03aab0d4";

  static final com.silenteight.data.api.v2.Alert ALERT_V2 =
      com.silenteight.data.api.v2.Alert.newBuilder()
          .setDiscriminator(ALERT_DISCRIMINATOR_1)
          .setPayload(PAYLOAD_1)
          .setName(ALERT_NAME_1)
          .build();

  static final com.silenteight.data.api.v2.ProductionDataIndexRequest PRODUCTION_REQUEST_V2 =
      com.silenteight.data.api.v2.ProductionDataIndexRequest.newBuilder()
          .addAllAlerts(of(ALERT_V2))
          .setRequestId(REQUEST_ID_V2)
          .build();
}
