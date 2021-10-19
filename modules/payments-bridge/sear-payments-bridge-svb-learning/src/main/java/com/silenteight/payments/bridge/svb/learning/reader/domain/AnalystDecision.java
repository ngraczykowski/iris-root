package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class AnalystDecision {

  String status;  // Taken from FKCO_STATUS
  OffsetDateTime actionDateTime;  // Taken from FKCO_D_ACTION_DATETIME
  String comment;  // FKCO_V_ACTION_COMMENT

  public String getActionDateTimeAsString() {
    return actionDateTime.toInstant().toString();
  }
}
