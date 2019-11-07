package com.silenteight.sens.webapp.domain.user;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.common.entity.AuditRevision;

import org.hibernate.envers.RevisionType;

@Data
@Builder
public class UserAudit {

  @NonNull
  private User user;
  @NonNull
  private AuditRevision auditRevision;
  @NonNull
  private RevisionType revisionType;
  private boolean passwordChanged;
}
