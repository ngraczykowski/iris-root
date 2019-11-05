package com.silenteight.sens.webapp.security.acl.exception;

public class InvalidPermissionException extends IllegalArgumentException {

  private static final long serialVersionUID = -6069273351211212721L;

  public InvalidPermissionException(String permissionName) {
    super("Permission " + permissionName + " is invalid");
  }
}
