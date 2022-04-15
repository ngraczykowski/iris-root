package com.silenteight.sens.webapp.scb.report;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuditHistoryEventDto {

  String username;
  String status;
  String ipAddress;
  long timestamp;
}
