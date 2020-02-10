package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.LastLoginTimeProvider;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
class InMemoryTestLastLoginTimeProvider implements LastLoginTimeProvider {

  private final Map<String, OffsetDateTime> repo = new HashMap<>();

  @Override
  public Optional<OffsetDateTime> getForUserId(String userId) {
    return Optional.ofNullable(repo.get(userId));
  }

  void add(String userId, OffsetDateTime time) {
    repo.put(userId, time);
  }
}
