package com.silenteight.sens.webapp.user;

import com.silenteight.sens.webapp.domain.user.UserToken;

import java.util.Optional;

interface UserTokenRepository {

  Optional<UserToken> find(String userName, String alias);

  Optional<UserToken> findByHashedToken(String hashedToken);

  UserToken save(UserToken userToken);

  void delete(UserToken userToken);
}
