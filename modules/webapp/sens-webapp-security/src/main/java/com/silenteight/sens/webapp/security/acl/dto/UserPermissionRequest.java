package com.silenteight.sens.webapp.security.acl.dto;

import lombok.Value;

import com.silenteight.sens.webapp.kernel.security.SensPermission;

import java.util.Set;

@Value
public class UserPermissionRequest {

  Object domainObject;

  long usedId;

  String authority;

  Set<SensPermission> permissions;
}
