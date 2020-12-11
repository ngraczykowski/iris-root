package com.silenteight.sens.webapp.scb.report;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuditHistoryEventDto {

  private String username;
  private String status;
  private String ipAddress;
  private long timestamp;
}
