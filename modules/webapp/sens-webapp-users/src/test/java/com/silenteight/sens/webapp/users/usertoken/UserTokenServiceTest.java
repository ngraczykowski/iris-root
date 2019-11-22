package com.silenteight.sens.webapp.users.usertoken;

import com.silenteight.sens.webapp.users.user.User;
import com.silenteight.sens.webapp.users.user.UserService;
import com.silenteight.sens.webapp.users.user.exception.UserNotFoundException;
import com.silenteight.sens.webapp.users.usertoken.exception.UserTokenAlreadyExistsException;
import com.silenteight.sens.webapp.users.usertoken.exception.UserTokenNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserTokenServiceTest {

  private static final String USER_NAME = "username";
  private static final String USER_TOKEN_ALIAS = "alias";

  @Mock
  private UserService userService;

  @Mock
  private UserTokenRepository userTokenRepository;

  @Mock
  private TokenEncoder tokenEncoder;

  @InjectMocks
  private UserTokenService classUnderTest;

  @Test
  public void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
    // given
    when(userService.getUserByName(USER_NAME)).thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> classUnderTest.generate(USER_NAME, USER_TOKEN_ALIAS))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  public void shouldThrowUserTokenAlreadyExistsExceptionWhenTokenIsPresentForUser() {
    // given
    User user = mock(User.class);
    UserToken userToken = mock(UserToken.class);
    when(userService.getUserByName(USER_NAME)).thenReturn(Optional.of(user));
    when(userTokenRepository.findOneByUserUserNameAndAlias(USER_NAME, USER_TOKEN_ALIAS))
        .thenReturn(Optional.of(userToken));

    // then
    assertThatThrownBy(() -> classUnderTest.generate(USER_NAME, USER_TOKEN_ALIAS))
        .isInstanceOf(UserTokenAlreadyExistsException.class);
  }

  @Test
  public void shouldGenerateUserToken() {
    // given
    User user = mock(User.class);
    when(userService.getUserByName(USER_NAME)).thenReturn(Optional.of(user));
    when(userTokenRepository.findOneByUserUserNameAndAlias(USER_NAME, USER_TOKEN_ALIAS))
        .thenReturn(Optional.empty());

    // when
    classUnderTest.generate(USER_NAME, USER_TOKEN_ALIAS);

    // then
    verify(tokenEncoder, times(1)).encode(anyString());
    verify(userTokenRepository, times(1)).save(any());
  }

  @Test
  public void shouldThrowUserTokenNotFoundExceptionWhenTokenNotPresentForUser() {
    // given
    when(userTokenRepository.findOneByUserUserNameAndAlias(USER_NAME, USER_TOKEN_ALIAS))
        .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> classUnderTest.delete(USER_NAME, USER_TOKEN_ALIAS))
        .isInstanceOf(UserTokenNotFoundException.class);
  }

  @Test
  public void shouldDeleteUserTokenIfExists() {
    // given
    UserToken userToken = mock(UserToken.class);
    when(userTokenRepository.findOneByUserUserNameAndAlias(USER_NAME, USER_TOKEN_ALIAS))
        .thenReturn(Optional.of(userToken));

    // when
    classUnderTest.delete(USER_NAME, USER_TOKEN_ALIAS);

    // then
    verify(userTokenRepository, times(1)).delete(userToken);
  }
}
