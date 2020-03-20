package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.user.password.ResetInternalUserPasswordUseCase.UserIsNotInternalException;
import com.silenteight.sens.webapp.user.password.ResetInternalUserPasswordUseCase.UserNotFoundException;
import com.silenteight.sens.webapp.user.password.TemporaryPassword;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

class UserRestControllerPasswordResettingTest extends UserRestControllerTest {

  private static final String USERNAME = "jdoe123";
  private static final String TEMP_PASSWORD = "password";

  @Test
  void its200WithTemporaryPassword_whenPasswordResetIsSuccessful() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willReturn(TemporaryPassword.of(TEMP_PASSWORD));

    patch(getRequestPath())
        .statusCode(OK.value())
        .body("temporaryPassword", is(TEMP_PASSWORD));
  }

  @Test
  void its404_whenUserIsNotFound() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willThrow(UserNotFoundException.class);

    patch(getRequestPath())
        .statusCode(NOT_FOUND.value());
  }

  @Test
  void its400_whenUserIsInternal() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willThrow(UserIsNotInternalException.class);

    patch(getRequestPath())
        .statusCode(BAD_REQUEST.value());
  }

  @Test
  void its500_whenRuntimeExceptionIsThrown() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willThrow(RuntimeException.class);

    patch(getRequestPath())
        .statusCode(INTERNAL_SERVER_ERROR.value());
  }

  private static String getRequestPath() {
    return "/users/" + USERNAME + "/password/reset";
  }
}
