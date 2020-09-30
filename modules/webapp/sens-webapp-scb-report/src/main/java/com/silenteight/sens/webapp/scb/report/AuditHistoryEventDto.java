package com.silenteight.sens.webapp.scb.report;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class AuditHistoryEventDto {

  private String userName;
  private String status;
  private String ipAddress;
  private Instant timestamp;
}
