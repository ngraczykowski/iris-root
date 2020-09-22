package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.user.update.exception.DisplayNameValidationException;
import com.silenteight.sep.usermanagement.api.RolesValidationException;
import com.silenteight.sep.usermanagement.api.UpdatedUserRepository.UserUpdateException;

import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.user.rest.dto.UpdateUserDtoFixtures.VALID_UPDATE_REQUEST;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@Import({ UserRestController.class, UserRestControllerAdvice.class })
@SuppressWarnings("squid:S2699")
class UpdateUserUseCaseRestControllerTest extends UserRestControllerTest {

  @TestWithRole(role = ADMINISTRATOR)
  void its201_whenUseCaseReturnsNoException() {
    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(NO_CONTENT.value());
  }

  @TestWithRole(role = ADMINISTRATOR)
  void its422_whenDisplayNameValidationException() {
    doThrow(DisplayNameValidationException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(UNPROCESSABLE_ENTITY.value());
  }

  @TestWithRole(role = ADMINISTRATOR)
  void its507_whenUpdateDisplayNameUseCaseDomainException() {
    doThrow(UserUpdateException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(INSUFFICIENT_STORAGE.value());
  }

  @TestWithRole(role = ADMINISTRATOR)
  void its422_whenRolesValidationException() {
    doThrow(RolesValidationException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(UNPROCESSABLE_ENTITY.value());
  }

  @TestWithRole(role = ADMINISTRATOR)
  void its500_whenOtherExceptionOccur() {
    doThrow(RuntimeException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(INTERNAL_SERVER_ERROR.value());
  }

  @TestWithRole(roles = { APPROVER, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(FORBIDDEN.value());
  }
}
