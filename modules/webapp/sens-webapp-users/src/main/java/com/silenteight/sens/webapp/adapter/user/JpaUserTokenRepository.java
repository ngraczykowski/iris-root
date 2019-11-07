package com.silenteight.sens.webapp.adapter.user;

import com.silenteight.sens.webapp.domain.user.UserToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserTokenRepository extends JpaRepository<UserToken, Long> {

  Optional<UserToken> findOneByUserUserNameAndAlias(String userName, String alias);

  Optional<UserToken> findOneByHashedToken(String hashedToken);
}
