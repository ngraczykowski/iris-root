package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.user.remove.OriginNotMatchingException;
import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase.RemoveUserCommand;
import com.silenteight.sens.webapp.user.remove.UserNotFoundException;

import org.mockito.ArgumentCaptor;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

class UserRestControllerRemoveUserTest extends UserRestControllerTest {

  private static final String USERNAME = "jdoe234";

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its204_whenUserDeleted() {
    delete(userResourcePath()).statusCode(NO_CONTENT.value());
  }

  @TestWithRole(role = USER_ADMINISTRATOR)
  void appliesCommandOnUseCase() {
    delete(userResourcePath());

    ArgumentCaptor<RemoveUserCommand> commandCaptor =
        ArgumentCaptor.forClass(RemoveUserCommand.class);
    verify(removeUserUseCase).apply(commandCaptor.capture());

    RemoveUserCommand command = commandCaptor.getValue();

    assertThat(command.getUsername()).isEqualTo(USERNAME);
    assertThat(command.getExpectedOrigin()).isEqualTo(SENS_ORIGIN);
  }

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its409_whenOriginDoesNotMatch() {
    willThrow(new OriginNotMatchingException())
        .given(removeUserUseCase).apply(any(RemoveUserCommand.class));

    delete(userResourcePath()).statusCode(CONFLICT.value());
  }

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its404_whenUserNotFound() {
    willThrow(new UserNotFoundException())
        .given(removeUserUseCase).apply(any(RemoveUserCommand.class));

    delete(userResourcePath()).statusCode(NOT_FOUND.value());
  }

  private static String userResourcePath() {
    return "/users/" + USERNAME;
  }
}
