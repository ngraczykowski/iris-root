package com.silenteight.sens.webapp.user.password;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
class TemporaryPasswordGenerator {

  private final int passwordLength;
  private final AlphanumericStringGenerator alphanumericGenerator;

  TemporaryPassword generate() {
    return TemporaryPassword.of(alphanumericGenerator.generate(passwordLength));
  }

  interface AlphanumericStringGenerator extends Function<Integer, String> {

    String generate(Integer size);

    @Override
    default String apply(Integer integer) {
      return generate(integer);
    }
  }
}
