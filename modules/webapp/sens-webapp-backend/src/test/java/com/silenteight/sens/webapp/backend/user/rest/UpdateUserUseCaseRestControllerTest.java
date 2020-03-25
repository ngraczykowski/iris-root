package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.user.update.UpdatedUserRepository.UserUpdateException;
import com.silenteight.sens.webapp.user.update.exception.DisplayNameValidationException;
import com.silenteight.sens.webapp.user.update.exception.RolesValidationException;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.USERNAME;
import static com.silenteight.sens.webapp.backend.user.rest.dto.UpdateUserDtoFixtures.VALID_UPDATE_REQUEST;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.INSUFFICIENT_STORAGE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Import({ UserRestController.class, UserRestControllerAdvice.class })
@SuppressWarnings("squid:S2699")
class UpdateUserUseCaseRestControllerTest extends UserRestControllerTest {

  @Test
  void its201_whenUseCaseReturnsNoException() {
    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(NO_CONTENT.value());
  }

  @Test
  void its422_whenDisplayNameValidationException() {
    doThrow(DisplayNameValidationException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(UNPROCESSABLE_ENTITY.value());
  }

  @Test
  void its507_whenUpdateDisplayNameUseCaseDomainException() {
    doThrow(UserUpdateException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(INSUFFICIENT_STORAGE.value());
  }

  @Test
  void its422_whenRolesValidationException() {
    doThrow(RolesValidationException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(UNPROCESSABLE_ENTITY.value());
  }

  @Test
  void its500_whenOtherExceptionOccur() {
    doThrow(RuntimeException.class).when(updateUserUseCase).apply(any());

    patch("/users/" + USERNAME, VALID_UPDATE_REQUEST).statusCode(INTERNAL_SERVER_ERROR.value());
  }
}
