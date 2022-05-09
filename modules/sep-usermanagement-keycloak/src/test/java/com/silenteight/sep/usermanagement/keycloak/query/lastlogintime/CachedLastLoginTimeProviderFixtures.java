package com.silenteight.sep.usermanagement.keycloak.query.lastlogintime;

import lombok.Value;
import lombok.experimental.UtilityClass;

import java.time.OffsetDateTime;
import java.util.Optional;
import javax.annotation.Nullable;

import static java.time.OffsetDateTime.parse;

@UtilityClass
class CachedLastLoginTimeProviderFixtures {

  static final LastLoginTime USER_1_LAST_LOGIN_TIME = new LastLoginTime(
      "30da08c2-6fcc-4350-8ba1-a5ba7798b857", parse("2011-12-03T10:15:30+01:00")
  );

  @Value
  static class LastLoginTime {

    String userId;
    @Nullable
    OffsetDateTime time;

    Optional<OffsetDateTime> getTimeOpt() {
      return Optional.ofNullable(time);
    }
  }
}
