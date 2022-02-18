package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.backend.user.rest.dto.CreateUserDto;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import io.vavr.control.Either;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USERNAME_NOT_UNIQUE;
import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USER_REGISTRATION_DOMAIN_ERROR;
import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USER_REGISTRATION_SUCCESS;
import static com.silenteight.sens.webapp.backend.user.rest.dto.CreateUserDtoFixtures.*;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.APPROVER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static io.vavr.control.Either.left;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;

class UserRestControllerInternalUserRegistrationTest extends UserRestControllerTest {

  private static final String USERS_URL = "/users";

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its422_whenUseCaseReturnsDomainException() {
    given(registerInternalUserUseCase.apply(any()))
        .willReturn(left(USER_REGISTRATION_DOMAIN_ERROR));

    post(USERS_URL, VALID_REQUEST)
        .statusCode(UNPROCESSABLE_ENTITY.value());
  }

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its409_whenUseCaseReturnsUsernameNotUnique() {
    given(registerInternalUserUseCase.apply(any()))
        .willReturn(left(USERNAME_NOT_UNIQUE));

    post(USERS_URL, VALID_REQUEST)
        .statusCode(CONFLICT.value());
  }

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its201WithValidLocationUri_whenUseCaseReturnsSuccess() {
    given(registerInternalUserUseCase.apply(any()))
        .willReturn(Either.right(USER_REGISTRATION_SUCCESS));

    post(USERS_URL, VALID_REQUEST)
        .statusCode(CREATED.value())
        .header("Location", Matchers.endsWith("/users/" + UserRestControllerFixtures.USERNAME));
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER })
  void its403_whenNotPermittedRole() {
    post(USERS_URL, VALID_REQUEST)
        .statusCode(FORBIDDEN.value());
  }

  @ParameterizedTest
  @MethodSource("getDtosWithInvalidFieldsLength")
  void its400WhenFieldLengthIsNotValid(CreateUserDto createUserDto) {
    post(USERS_URL, createUserDto)
        .statusCode(BAD_REQUEST.value());
  }

  private static Stream<CreateUserDto> getDtosWithInvalidFieldsLength() {
    return Stream.of(
        INVALID_REQUEST_WITH_TOO_LONG_USER_NAME,
        INVALID_REQUEST_WITH_TOO_SHORT_USER_NAME,
        INVALID_REQUEST_WITH_TOO_LONG_DISPLAY_NAME,
        INVALID_REQUEST_WITH_TOO_SHORT_DISPLAY_NAME
    );
  }
}
