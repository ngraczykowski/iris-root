package com.silenteight.sens.webapp.security.acl.exception;

import org.springframework.security.acls.model.ObjectIdentity;

public class AccessListNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -6435092722633279410L;

  public AccessListNotFoundException(ObjectIdentity identity, Throwable cause) {
    super("Access list for " + identity + " not found", cause);
  }
}
