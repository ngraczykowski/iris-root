package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.backend.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase.UserIsNotInternalException;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase.UserNotFoundException;
import com.silenteight.sens.webapp.user.password.reset.TemporaryPassword;

import static com.silenteight.sens.webapp.backend.rest.TestRoles.ADMIN;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.BUSINESS_OPERATOR;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;

class UserRestControllerPasswordResettingTest extends UserRestControllerTest {

  private static final String USERNAME = "jdoe123";
  private static final String TEMP_PASSWORD = "password";

  @TestWithRole(role = ADMIN)
  void its200WithTemporaryPassword_whenPasswordResetIsSuccessful() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willReturn(TemporaryPassword.of(TEMP_PASSWORD));

    patch(getRequestPath())
        .statusCode(OK.value())
        .body("temporaryPassword", is(TEMP_PASSWORD));
  }

  @TestWithRole(role = ADMIN)
  void its404_whenUserIsNotFound() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willThrow(UserNotFoundException.class);

    patch(getRequestPath())
        .statusCode(NOT_FOUND.value());
  }

  @TestWithRole(role = ADMIN)
  void its400_whenUserIsInternal() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willThrow(UserIsNotInternalException.class);

    patch(getRequestPath())
        .statusCode(BAD_REQUEST.value());
  }

  @TestWithRole(role = ADMIN)
  void its500_whenRuntimeExceptionIsThrown() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willThrow(RuntimeException.class);

    patch(getRequestPath())
        .statusCode(INTERNAL_SERVER_ERROR.value());
  }

  @TestWithRole(roles = { ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    patch(getRequestPath()).statusCode(FORBIDDEN.value());
  }

  private static String getRequestPath() {
    return "/users/" + USERNAME + "/password/reset";
  }
}
