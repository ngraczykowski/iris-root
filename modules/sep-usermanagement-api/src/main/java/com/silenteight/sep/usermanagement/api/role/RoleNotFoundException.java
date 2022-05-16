package com.silenteight.sep.usermanagement.api.role;

import lombok.NonNull;

public class RoleNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -8575226276120649160L;

  public RoleNotFoundException(@NonNull String roleName) {
    super(String.format("Role with name %s was not found", roleName));
  }
}
