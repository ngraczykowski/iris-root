package com.silenteight.sens.webapp.user.roles;

import com.silenteight.sens.webapp.user.list.ListUsersWithRoleUseCase;
import com.silenteight.sens.webapp.user.list.UserListDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.sens.webapp.user.roles.RolesTestFixtures.ROLE_NAME;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRolesValidatorTest {

  @InjectMocks
  private UserRolesValidator underTest;

  @Mock
  private ListUsersWithRoleUseCase listUsersWithRoleUseCase;

  @Test
  void anyUserHasRoleIsFalse() {
    // given
    when(listUsersWithRoleUseCase.apply(ROLE_NAME)).thenReturn(emptyList());

    // when
    boolean result = underTest.isAssigned(ROLE_NAME);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void anyUserHasRoleIsTrue() {
    // given
    UserListDto user = UserListDto.builder()
        .userName("jsmith")
        .origin("SENS")
        .roles(List.of(ROLE_NAME))
        .build();
    when(listUsersWithRoleUseCase.apply(ROLE_NAME)).thenReturn(singletonList(user));

    // when
    boolean result = underTest.isAssigned(ROLE_NAME);

    // then
    assertThat(result).isTrue();
  }
}
