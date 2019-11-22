package com.silenteight.sens.webapp.users.user.dto;

import com.silenteight.sens.webapp.users.user.User;
import com.silenteight.sens.webapp.users.user.UserAudit;

import java.time.Instant;

public class UserAuditDtoMapper {

  public UserAuditDto map(UserAudit audit) {
    User user = audit.getUser();
    return UserAuditDto
        .builder()
        .userId(user.getId())
        .userName(user.getUserName())
        .displayName(user.getDisplayName())
        .passwordChanged(audit.isPasswordChanged())
        .superUser(user.isSuperUser())
        .active(user.isActive())
        .roles(user.getRoles())
        .revisionType(audit.getRevisionType())
        .auditedAt(Instant.ofEpochMilli(audit.getAuditRevision().getTimestamp()))
        .modifiedBy(audit.getAuditRevision().getUserName())
        .build();
  }
}
