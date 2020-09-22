package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sep.usermanagement.api.UserDomainError;

import io.vavr.control.Either;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.*;

class ResultAssert
    extends AbstractAssert<ResultAssert,
    Either<UserDomainError, RegisterInternalUserUseCase.Success>> {

  private ResultAssert(
      Either<UserDomainError, RegisterInternalUserUseCase.Success> result) {
    super(result, ResultAssert.class);
  }

  static ResultAssert assertThatResult(
      Either<UserDomainError, RegisterInternalUserUseCase.Success> result) {
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

  ResultAssert containsErrorReason(String errorReason) {
    assertThat(actual.getLeft().getReason()).isEqualTo(errorReason);

    return this;
  }
}
