package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import io.vavr.control.Either;
import org.hamcrest.Matchers;

import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USERNAME_NOT_UNIQUE;
import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USER_REGISTRATION_DOMAIN_ERROR;
import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USER_REGISTRATION_SUCCESS;
import static com.silenteight.sens.webapp.backend.user.rest.dto.CreateUserDtoFixtures.VALID_REQUEST;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.APPROVER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static io.vavr.control.Either.left;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

class UserRestControllerInternalUserRegistrationTest extends UserRestControllerTest {

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its422_whenUseCaseReturnsDomainException() {
    given(registerInternalUserUseCase.apply(any()))
        .willReturn(left(USER_REGISTRATION_DOMAIN_ERROR));

    post("/users", VALID_REQUEST)
        .statusCode(UNPROCESSABLE_ENTITY.value());
  }

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its409_whenUseCaseReturnsUsernameNotUnique() {
    given(registerInternalUserUseCase.apply(any()))
        .willReturn(left(USERNAME_NOT_UNIQUE));

    post("/users", VALID_REQUEST)
        .statusCode(CONFLICT.value());
  }

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its201WithValidLocationUri_whenUseCaseReturnsSuccess() {
    given(registerInternalUserUseCase.apply(any()))
        .willReturn(Either.right(USER_REGISTRATION_SUCCESS));

    post("/users", VALID_REQUEST)
        .statusCode(CREATED.value())
        .header("Location", Matchers.endsWith("/users/" + UserRestControllerFixtures.USERNAME));
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER })
  void its403_whenNotPermittedRole() {
    post("/users", VALID_REQUEST)
        .statusCode(FORBIDDEN.value());
  }
}
