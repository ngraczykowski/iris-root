package com.silenteight.sens.webapp.user;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.adapter.user.JpaUserTokenRepository;
import com.silenteight.sens.webapp.domain.user.UserToken;

import java.util.Optional;

@RequiredArgsConstructor
class DatabaseUserTokenRepository implements UserTokenRepository {

  private final JpaUserTokenRepository userTokenRepository;

  @Override
  public Optional<UserToken> find(String userName, String alias) {
    return userTokenRepository.findOneByUserUserNameAndAlias(userName, alias);
  }

  @Override
  public Optional<UserToken> findByHashedToken(String hashedToken) {
    return userTokenRepository.findOneByHashedToken(hashedToken);
  }

  @Override
  public UserToken save(UserToken userToken) {
    return userTokenRepository.save(userToken);
  }

  @Override
  public void delete(UserToken userToken) {
    userTokenRepository.delete(userToken);
  }
}
