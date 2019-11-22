package com.silenteight.sens.webapp.users.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.common.audit.AuditDto;
import com.silenteight.sens.webapp.kernel.security.authority.Role;

import org.hibernate.envers.RevisionType;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Data
@Builder
public class UserAuditDto implements AuditDto {

  @NonNull
  private final Long userId;
  @NonNull
  private final String userName;
  private final String displayName;
  private final boolean passwordChanged;
  private final boolean superUser;
  private final boolean active;
  @NonNull
  private final List<Role> roles;
  @NonNull
  private final RevisionType revisionType;
  @NonNull
  private final Instant auditedAt;
  private final String modifiedBy;

  public String getRolesString() {
    return roles.stream().map(Enum::name).collect(joining(","));
  }
}
