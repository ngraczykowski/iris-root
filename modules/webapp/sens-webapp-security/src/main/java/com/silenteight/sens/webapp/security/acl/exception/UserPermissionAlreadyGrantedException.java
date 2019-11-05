package com.silenteight.sens.webapp.security.acl.exception;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

public class UserPermissionAlreadyGrantedException extends RuntimeException {

  private static final long serialVersionUID = 8459255710738555589L;

  public UserPermissionAlreadyGrantedException(Sid sid, Permission permission) {
    super("User permission " + permission + " for sid " + sid + " already granted");
  }
}
