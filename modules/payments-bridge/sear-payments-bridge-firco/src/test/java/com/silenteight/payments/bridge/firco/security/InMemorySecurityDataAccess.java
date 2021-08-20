package com.silenteight.payments.bridge.firco.security;

class InMemorySecurityDataAccess implements SecurityDataAccess {

  @Override
  public boolean findByCredentials(String userName, String password) {
    return userName.equals("username") && password.equals("password");
  }
}
