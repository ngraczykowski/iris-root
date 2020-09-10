package com.silenteight.sep.keycloak.query.role;

import lombok.NonNull;

import java.util.List;

public interface RolesProvider {

  @NonNull
  List<String> getForUserId(String userId);
}
