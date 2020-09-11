package com.silenteight.sep.usermanagement.keycloak.query.role;

import lombok.NonNull;

import java.util.List;

public interface RolesProvider {

  @NonNull
  List<String> getForUserId(String userId);
}
