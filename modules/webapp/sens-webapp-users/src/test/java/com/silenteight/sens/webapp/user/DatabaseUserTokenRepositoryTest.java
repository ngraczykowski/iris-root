package com.silenteight.sens.webapp.user;

import com.silenteight.sens.webapp.adapter.user.JpaUserTokenRepository;
import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.domain.user.UserToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseUserTokenRepositoryTest {

  private static final String USER_NAME = "user_name";
  private static final String HASHED_TOKEN = "hashed_token";
  private static final String TOKEN_ALIAS = "token_alias";

  @Mock
  private JpaUserTokenRepository jpaUserTokenRepository;

  private UserTokenRepository userTokenRepository;

  private UserToken userToken;

  @Before
  public void setUp() {
    userTokenRepository = new DatabaseUserTokenRepository(jpaUserTokenRepository);
    userToken = getUserToken();
  }

  @Test
  public void findingUserTokenByUserNameAndAlias_willFindInJpaRepository() {
    //given
    when(jpaUserTokenRepository.findOneByUserUserNameAndAlias(USER_NAME, TOKEN_ALIAS))
        .thenReturn(Optional.of(userToken));

    //when
    Optional<UserToken> foundUserToken = userTokenRepository.find(USER_NAME, TOKEN_ALIAS);

    //then
    assertThat(foundUserToken).isEqualTo(Optional.of(userToken));
  }

  @Test
  public void findingUserTokenByHashedToken_willFindInJpaRepository() {
    //given
    when(jpaUserTokenRepository.findOneByHashedToken(HASHED_TOKEN))
        .thenReturn(Optional.of(userToken));

    //when
    Optional<UserToken> foundUserToken = userTokenRepository.findByHashedToken(HASHED_TOKEN);

    //then
    assertThat(foundUserToken).isEqualTo(Optional.of(userToken));
  }

  @Test
  public void savingUserToken_willSaveToJpaRepository() {
    //given
    ArgumentCaptor<UserToken> captor = ArgumentCaptor.forClass(UserToken.class);
    when(jpaUserTokenRepository.save(captor.capture())).thenAnswer(p -> p.getArguments()[0]);

    //when
    UserToken savedUserToken = userTokenRepository.save(userToken);

    //then
    assertThat(captor.getValue())
        .isEqualTo(savedUserToken)
        .extracting(UserToken::getAlias)
        .isEqualTo(TOKEN_ALIAS);
  }

  @Test
  public void deletingUserToken_willDeleteFromJpaRepository() {
    //when
    userTokenRepository.delete(userToken);

    //then
    verify(jpaUserTokenRepository, times(1)).delete(userToken);
  }

  private UserToken getUserToken() {
    User user = new User(USER_NAME);
    return new UserToken(user, HASHED_TOKEN, TOKEN_ALIAS);
  }
}
