package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.backend.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.user.update.UpdatedUserRepository.UserUpdateException;
import com.silenteight.sens.webapp.user.update.exception.DisplayNameValidationException;
import com.silenteight.sens.webapp.user.update.exception.RolesValidationException;

import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.rest.TestRoles.ADMIN;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USERNAME;
import static com.silenteight.sens.webapp.backend.user.rest.dto.UpdateUserDtoFixtures.VALID_UPDATE_REQUEST;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@Import({ UserRestController.class, UserRestControllerAdvice.class })
@SuppressWarnings("squid:S2699")
class UpdateUserUseCaseRestControllerTest extends UserRestControllerTest {

  @TestWithRole(role = ADMIN)
  void its201_whenUseCaseReturnsNoException() {
    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(NO_CONTENT.value());
  }

  @TestWithRole(role = ADMIN)
  void its422_whenDisplayNameValidationException() {
    doThrow(DisplayNameValidationException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(UNPROCESSABLE_ENTITY.value());
  }

  @TestWithRole(role = ADMIN)
  void its507_whenUpdateDisplayNameUseCaseDomainException() {
    doThrow(UserUpdateException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(INSUFFICIENT_STORAGE.value());
  }

  @TestWithRole(role = ADMIN)
  void its422_whenRolesValidationException() {
    doThrow(RolesValidationException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(UNPROCESSABLE_ENTITY.value());
  }

  @TestWithRole(role = ADMIN)
  void its500_whenOtherExceptionOccur() {
    doThrow(RuntimeException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(INTERNAL_SERVER_ERROR.value());
  }

  @TestWithRole(roles = { ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(FORBIDDEN.value());
  }
}
