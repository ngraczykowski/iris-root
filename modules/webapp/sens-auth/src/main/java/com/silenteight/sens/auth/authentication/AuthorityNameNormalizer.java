package com.silenteight.sens.auth.authentication;

import java.util.function.Function;

@FunctionalInterface
interface AuthorityNameNormalizer extends Function<String, String> {

  default String normalize(String authority) {
    return apply(authority);
  }
}
