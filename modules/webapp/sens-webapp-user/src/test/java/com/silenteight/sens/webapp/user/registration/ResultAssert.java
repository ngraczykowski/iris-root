package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.user.registration.domain.UserRegistrationDomainError;

import io.vavr.control.Either;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.*;

class ResultAssert
    extends AbstractAssert<ResultAssert,
    Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success>> {

  private ResultAssert(
      Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> result) {
    super(result, ResultAssert.class);
  }

  static ResultAssert assertThatResult(
      Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> result) {
    return new ResultAssert(result);
  }

  ResultAssert isSuccessWithUsername(String username) {
    assertThat(actual.isRight()).isTrue();
    assertThat(actual)
        .extracting(RegisterInternalUserUseCase.Success::getUsername)
        .containsOnly(username);

    return this;
  }

  ResultAssert isFailureOfType(Class<?> type) {
    assertThat(actual.isLeft()).isTrue();
    assertThat(actual.getLeft()).isInstanceOf(type);

    return this;
  }
}
