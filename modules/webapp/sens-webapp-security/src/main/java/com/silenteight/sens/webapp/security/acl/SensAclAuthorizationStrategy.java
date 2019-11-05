package com.silenteight.sens.webapp.security.acl;


import com.silenteight.sens.webapp.kernel.security.SensSecurityContext;

import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.model.Acl;

class SensAclAuthorizationStrategy implements AclAuthorizationStrategy {

  @Override
  public void securityCheck(Acl acl, int changeType) {
    switch (changeType) {
      default:
      case CHANGE_GENERAL:
      case CHANGE_OWNERSHIP:
        return;

      case CHANGE_AUDITING:
        // TODO(ahaczewski): check ROLE_AUDIT.
        SensSecurityContext.requireUser().getAuthorities();
    }
  }
}
