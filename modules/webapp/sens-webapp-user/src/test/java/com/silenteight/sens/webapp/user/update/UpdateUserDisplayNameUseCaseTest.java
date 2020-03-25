package com.silenteight.sens.webapp.user.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCaseFixtures.NEW_DISPLAY_NAME_COMMAND;
import static com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCaseFixtures.OFFSET_DATE_TIME;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserDisplayNameUseCaseTest {

  @Mock
  private UpdatedUserRepository updatedUserRepository;

  private UpdateUserDisplayNameUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserUpdateUseCaseConfiguration()
        .updateUserDisplayNameUseCase(updatedUserRepository);
  }

  @Test
  void updateDisplayNameCommand_updateUser() {
    // when
    underTest.apply(NEW_DISPLAY_NAME_COMMAND);

    // then
    verify(updatedUserRepository).save(
        updatedUser(
            NEW_DISPLAY_NAME_COMMAND.getUsername(), NEW_DISPLAY_NAME_COMMAND.getDisplayName()));
  }

  private static UpdatedUser updatedUser(String username, String displayName) {
    return UpdatedUser
        .builder()
        .username(username)
        .displayName(displayName)
        .updateDate(OFFSET_DATE_TIME)
        .build();
  }
}
