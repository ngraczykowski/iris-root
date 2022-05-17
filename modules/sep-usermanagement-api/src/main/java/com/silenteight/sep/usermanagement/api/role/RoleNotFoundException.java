package com.silenteight.sep.usermanagement.api.role;

import lombok.NonNull;

import org.jetbrains.annotations.NotNull;

public class RoleNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -8575226276120649160L;

  public RoleNotFoundException(@NotNull String roleName) {
    this(roleName, null);
  }

  public RoleNotFoundException(@NonNull String roleName, Throwable cause) {
    super(String.format("Role with name %s was not found", roleName), cause);
  }
}
