package com.silenteight.payments.bridge.warehouse.index.model.payload;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class WarehouseAlert implements IndexPayload {

  // PB's own AlertMessageStatusEntity.alertMessageId for CMAPI and CMAPI+CSV OR
  // alertName for CSV-only
  // alert-messages/<UUID> from CMAPI (firco)
  // alerts/12345 from CSV (svb-learning)
  @NonNull
  String alertMessageId;
  @NonNull
  String fircoSystemId;  // From FKCO_V_SYSTEM_ID OR CMAPI [Message][SystemID]
  String status;  // PB's own AlertMessageStatusEntity.status
}
