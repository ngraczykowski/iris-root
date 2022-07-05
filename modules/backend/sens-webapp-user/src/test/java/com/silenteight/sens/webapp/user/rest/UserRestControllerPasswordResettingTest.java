/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.sens.webapp.user.rest;

import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase.UserIsNotInternalException;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase.UserNotFoundException;
import com.silenteight.sep.usermanagement.api.credentials.dto.TemporaryPassword;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;

class UserRestControllerPasswordResettingTest extends UserRestControllerTest {

  private static final String USERNAME = "jdoe123";
  private static final String TEMP_PASSWORD = "password";

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its200WithTemporaryPassword_whenPasswordResetIsSuccessful() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willReturn(TemporaryPassword.of(TEMP_PASSWORD));

    patch(getRequestPath())
        .statusCode(OK.value())
        .body("temporaryPassword", is(TEMP_PASSWORD));
  }

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its404_whenUserIsNotFound() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willThrow(UserNotFoundException.class);

    patch(getRequestPath())
        .statusCode(NOT_FOUND.value());
  }

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its400_whenUserIsInternal() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willThrow(UserIsNotInternalException.class);

    patch(getRequestPath())
        .statusCode(BAD_REQUEST.value());
  }

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its500_whenRuntimeExceptionIsThrown() {
    given(resetInternalUserPasswordUseCase.execute(USERNAME))
        .willThrow(RuntimeException.class);

    patch(getRequestPath())
        .statusCode(INTERNAL_SERVER_ERROR.value());
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    patch(getRequestPath()).statusCode(FORBIDDEN.value());
  }

  private static String getRequestPath() {
    return "/users/" + USERNAME + "/password/reset";
  }
}
