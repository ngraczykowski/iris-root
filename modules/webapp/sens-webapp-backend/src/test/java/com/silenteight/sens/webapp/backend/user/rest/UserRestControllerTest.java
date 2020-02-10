package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.backend.user.UserQuery;
import com.silenteight.sens.webapp.backend.user.registration.RegisterInternalUserUseCase;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;

import io.vavr.control.Either;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USERNAME;
import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USERNAME_NOT_UNIQUE;
import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USER_REGISTRATION_DOMAIN_ERROR;
import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USER_REGISTRATION_SUCCESS;
import static com.silenteight.sens.webapp.backend.user.rest.dto.CreateUserDtoFixtures.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@Import({ UserRestController.class, UserRestControllerAdvice.class })
class UserRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private RegisterInternalUserUseCase registerInternalUserUseCase;

  @MockBean
  private UserQuery userQuery;

  @Nested
  class InternalRegistrationUseCaseTests {

    @Test
    void its422_whenUseCaseReturnsDomainException() {
      given(registerInternalUserUseCase.apply(any()))
          .willReturn(Either.left(USER_REGISTRATION_DOMAIN_ERROR));

      post("/users", VALID_REQUEST)
          .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void its409_whenUseCaseReturnsUsernameNotUnique() {
      given(registerInternalUserUseCase.apply(any()))
          .willReturn(Either.left(USERNAME_NOT_UNIQUE));

      post("/users", VALID_REQUEST)
          .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void its201WithValidLocationUri_whenUseCaseReturnsSuccess() {
      given(registerInternalUserUseCase.apply(any()))
          .willReturn(Either.right(USER_REGISTRATION_SUCCESS));

      post("/users", VALID_REQUEST)
          .statusCode(HttpStatus.CREATED.value())
          .header("Location", Matchers.endsWith("/users/" + USERNAME));
    }
  }
}
