package com.silenteight.sens.webapp.security.acl.dto;

import lombok.Value;

@Value
public class PermissionView {

  String objectIdentifier;

  String permission;
}
