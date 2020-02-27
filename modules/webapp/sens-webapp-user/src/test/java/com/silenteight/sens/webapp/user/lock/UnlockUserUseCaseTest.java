package com.silenteight.sens.webapp.user.lock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.user.lock.UnlockUserUseCaseFixtures.UNLOCK_COMMAND;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnlockUserUseCaseTest {

  @Mock
  private UserLocker userLocker;

  private UnlockUserUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserLockUseCaseConfiguration().unlockUserUseCase(userLocker);
  }

  @Test
  void unlockUserCommand_unlockUserByUsername() {
    // when
    underTest.apply(UNLOCK_COMMAND);

    // then
    verify(userLocker).unlock(UNLOCK_COMMAND.getUsername());
  }
}
