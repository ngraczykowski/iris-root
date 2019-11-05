package com.silenteight.sens.webapp.security.acl;

import com.silenteight.sens.webapp.kernel.security.SensPermission;

import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.model.Permission;

import java.util.HashMap;
import java.util.Map;

class AccessPermissionFactory extends DefaultPermissionFactory {

  private static final Map<String, Permission> SENS_PERMISSIONS;

  static {
    SENS_PERMISSIONS = new HashMap<>();
    for (SensPermission value : SensPermission.values())
      SENS_PERMISSIONS.put(value.name(), new AccessPermission(value));
  }

  AccessPermissionFactory() {
    super(SENS_PERMISSIONS);
  }
}
