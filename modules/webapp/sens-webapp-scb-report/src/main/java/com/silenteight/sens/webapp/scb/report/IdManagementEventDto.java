package com.silenteight.sens.webapp.scb.report;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class IdManagementEventDto {

  private String username;
  private String role;
  private String principal;
  private Instant timestamp;
  private String action;
}
