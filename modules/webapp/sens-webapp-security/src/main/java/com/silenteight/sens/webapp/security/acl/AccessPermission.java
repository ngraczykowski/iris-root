package com.silenteight.sens.webapp.security.acl;

import com.silenteight.sens.webapp.kernel.security.SensPermission;

import org.springframework.security.acls.domain.AbstractPermission;

class AccessPermission extends AbstractPermission {

  private static final long serialVersionUID = -3391162592127877407L;

  AccessPermission(SensPermission sensPermission) {
    super(sensPermission.getMask(), sensPermission.getCode());
  }
}
