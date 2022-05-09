package com.silenteight.sep.usermanagement.api.role;

import lombok.NonNull;

import java.util.List;
import java.util.Set;

public interface UserRoles {

  @NonNull
  Set<String> getRoles(@NonNull String scope);

  @NonNull
  List<String> getSortedRoles(@NonNull String scope);

  @NonNull
  Set<String> getScopes();
}
