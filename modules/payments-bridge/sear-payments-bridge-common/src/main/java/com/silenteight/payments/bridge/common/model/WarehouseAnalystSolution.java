package com.silenteight.payments.bridge.common.model;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class WarehouseAnalystSolution {

  String fircoAnalystStatus;  // Taken from FKCO_STATUS
  String fircoAnalystDecision;  // Translated from FKCO_STATUS to FP/PTP
  String fircoAnalystDecisionTime;  // Taken from FKCO_D_ACTION_DATETIME
  String fircoAnalystComment;  // FKCO_V_ACTION_COMMENT
  String accessPermissionTag;
}
