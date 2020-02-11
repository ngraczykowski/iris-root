package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface LastLoginTimeProvider {

  Optional<OffsetDateTime> getForUserId(String userId);
}
