package com.silenteight.sep.usermanagement.keycloak.query.lastlogintime;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface LastLoginTimeProvider {

  Optional<OffsetDateTime> getForUserId(String userId);
}
