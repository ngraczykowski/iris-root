package com.silenteight.sens.webapp.scb.report;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserAuthActivityEventDto {

  private String username;
  private List<String> roles;
  private String ipAddress;
  private long loginTimestamp;
  private long logoutTimestamp;
}
