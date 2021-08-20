package com.silenteight.payments.bridge.firco.security;

public interface SecurityDataAccess {

  boolean findByCredentials(String userName, String password);
}
