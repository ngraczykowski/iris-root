package com.silenteight.payments.bridge.warehouse.index.model.payload;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class WarehouseAnalystSolution implements IndexPayload {

  String fircoAnalystStatus;  // Taken from FKCO_STATUS
  String fircoAnalystDecision;  // Translated from FKCO_STATUS to FP/PTP
  String fircoAnalystDecisionTime;  // Taken from FKCO_D_ACTION_DATETIME
  String fircoAnalystComment;  // FKCO_V_ACTION_COMMENT
}
