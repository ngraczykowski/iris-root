package com.silenteight.sens.webapp.user.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.silenteight.sens.webapp.user.update.AddRolesToUserUseCaseFixtures.ADD_ANALYST_ROLE_COMMAND;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddRolesToUserUseCaseTest {

  @Mock
  private UpdatedUserRepository updatedUserRepository;

  private AddRolesToUserUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserUpdateUseCaseConfiguration()
        .addRolesToUserUseCase(updatedUserRepository);
  }

  @Test
  void addRoleToUserCommand_updateUser() {
    // when
    underTest.apply(ADD_ANALYST_ROLE_COMMAND);

    // then
    verify(updatedUserRepository).save(
        updatedUser(
            ADD_ANALYST_ROLE_COMMAND.getUsername(), ADD_ANALYST_ROLE_COMMAND.getRoles()));
  }

  private static UpdatedUser updatedUser(String username, Set<String> roles) {
    return UpdatedUser
        .builder()
        .username(username)
        .roles(roles)
        .build();
  }
}
