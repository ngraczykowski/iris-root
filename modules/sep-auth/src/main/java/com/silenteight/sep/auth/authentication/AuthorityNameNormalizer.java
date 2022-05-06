package com.silenteight.sep.auth.authentication;

import java.util.function.Function;

@Deprecated
@FunctionalInterface
public interface AuthorityNameNormalizer extends Function<String, String> {

  default String normalize(String authority) {
    return apply(authority);
  }
}
