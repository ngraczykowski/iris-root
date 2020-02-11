package com.silenteight.sens.webapp.keycloak.authentication;

import java.util.function.Function;

@FunctionalInterface
interface AuthorityNameNormalizer extends Function<String, String> {

  default String normalize(String authority) {
    return apply(authority);
  }
}
