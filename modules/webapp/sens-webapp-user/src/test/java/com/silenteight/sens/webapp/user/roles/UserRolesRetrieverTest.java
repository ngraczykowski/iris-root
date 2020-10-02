package com.silenteight.sens.webapp.user.roles;

import com.silenteight.sep.usermanagement.api.UserQuery;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRolesRetrieverTest {

  @InjectMocks
  private UserRolesRetriever underTest;

  @Mock
  private UserQuery userQuery;

  @Test
  void retrieveRoles() {
    // given
    String username = "jsmith";
    List<String> roles = List.of("APPROVER", "ANALYST", "BUSINESS_OPERATOR");
    when(userQuery.find(username)).thenReturn(of(user(roles)));

    // when
    List<String> results = underTest.rolesOf(username);

    // then
    assertThat(results).isEqualTo(roles);
  }

  @Test
  void throwsUserNotFoundExceptionIfUserDoesNotExist() {
    String username = "jsmith";
    when(userQuery.find(username)).thenThrow(new UserNotFoundException());

    ThrowingCallable rolesRetrievalCall = () -> underTest.rolesOf(username);

    assertThatThrownBy(rolesRetrievalCall).isInstanceOf(UserNotFoundException.class);
  }

  private UserDto user(List<String> roles) {
    UserDto userDto = new UserDto();
    userDto.setRoles(roles);
    return userDto;
  }
}
