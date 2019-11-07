package com.silenteight.sens.webapp.domain.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUserUtil {

  public static void setUserId(User user, Long id) {
    user.setId(id);
  }
}
