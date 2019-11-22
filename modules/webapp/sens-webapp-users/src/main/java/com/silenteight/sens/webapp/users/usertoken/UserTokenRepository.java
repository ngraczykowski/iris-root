package com.silenteight.sens.webapp.users.usertoken;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface UserTokenRepository extends Repository<UserToken, Long> {

  UserToken save(UserToken entity);

  Optional<UserToken> findOneByUserUserNameAndAlias(String userName, String alias);

  Optional<UserToken> findOneByHashedToken(String hashedToken);

  void delete(UserToken change);
}
