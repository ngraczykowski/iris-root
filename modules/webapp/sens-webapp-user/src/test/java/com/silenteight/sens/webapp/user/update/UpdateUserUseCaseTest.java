package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.api.AuditLog;

import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.user.update.UpdateUserUseCaseFixtures.UPDATED_USER;
import static com.silenteight.sens.webapp.user.update.UpdateUserUseCaseFixtures.UPDATE_USER_COMMAND;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

  @Mock
  private UpdatedUserRepository updatedUserRepository;

  @Mock
  private AuditLog auditLog;

  private UpdateUserUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserUpdateUseCaseConfiguration().updateUserUseCase(
        updatedUserRepository, roles -> Option.none(), displayName -> Option.none(), auditLog);
  }

  @Test
  void updateDisplayNameCommand_updateUser() {
    // when
    underTest.apply(UPDATE_USER_COMMAND);

    // then
    verify(updatedUserRepository).save(UPDATED_USER);
  }
}
