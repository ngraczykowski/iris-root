package com.silenteight.sens.webapp.scb.report;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Collection;

import static java.lang.String.join;

@Value
@Builder
public class IdManagementEventDto {

  private static final String DELIMITER = ",";
  private static final String NO_ROLE = "NO_ROLE";

  String username;
  Collection<String> roles;
  String principal;
  Instant timestamp;
  String action;

  String getRolesAsString() {
    return roles.isEmpty() ? NO_ROLE : join(DELIMITER, roles);
  }
}
