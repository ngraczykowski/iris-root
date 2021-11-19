package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Value
public class AnalystDecision {

  String status;  // Taken from FKCO_V_STATUS_NAME
  OffsetDateTime actionDateTime;  // Taken from FKCO_D_ACTION_DATETIME
  String comment;  // FKCO_V_ACTION_COMMENT

  List<String> previousStatuses;

  public String getActionDateTimeAsString() {
    return actionDateTime.toInstant().toString();
  }
}
