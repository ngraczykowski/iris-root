package com.silenteight.sens.webapp.users.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.users.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUserUtil {

  public static void setUserId(User user, Long id) {
    user.setId(id);
  }
}
