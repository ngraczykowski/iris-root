package com.silenteight.sens.webapp.user.lock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.user.lock.LockUserUseCaseFixtures.LOCK_COMMAND;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LockUserUseCaseTest {

  @Mock
  private UserLocker userLocker;

  private LockUserUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserLockUseCaseConfiguration().lockUserUseCase(userLocker);
  }

  @Test
  void lockUserCommand_lockUserByUsername() {
    // when
    underTest.apply(LOCK_COMMAND);

    // then
    verify(userLocker).lock(LOCK_COMMAND.getUsername());
  }
}
