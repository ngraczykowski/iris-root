package com.silenteight.sep.usermanagement.keycloak.query.role;

import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RolesProvider {

  @NonNull
  Map<String, List<String>> getForUserId(String userId, Set<String> roleClientIds);
}
