package com.silenteight.payments.bridge.firco.security.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.security.SecurityDataAccess;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
class JdbcSecurityDataAccess implements SecurityDataAccess {

  private final SelectByCredentialsQuery selectByCredentialsQuery;

  @Override
  public boolean findByCredentials(@Nonnull String userName, @Nonnull String password) {
    return selectByCredentialsQuery.execute(userName, password);
  }
}
