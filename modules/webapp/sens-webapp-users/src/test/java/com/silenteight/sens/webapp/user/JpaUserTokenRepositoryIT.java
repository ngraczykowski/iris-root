package com.silenteight.sens.webapp.user;

import com.silenteight.sens.webapp.adapter.user.JpaUserTokenRepository;
import com.silenteight.sens.webapp.common.database.DataSourceAutoConfiguration;
import com.silenteight.sens.webapp.common.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.domain.user.UserToken;
import com.silenteight.sens.webapp.user.JpaUserTokenRepositoryIT.TestConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = { PostgresTestInitializer.class }, classes = {
    DataSourceAutoConfiguration.class,
    TestConfiguration.class
})
@DataJpaTest
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureTestDatabase(replace = NONE)
public class JpaUserTokenRepositoryIT {

  private static final String USER_NAME = "user_name";
  private static final String TOKEN = "token";
  private static final String TOKEN_ALIAS = "token_alias";

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private JpaUserTokenRepository userTokenRepository;

  private TokenEncoder tokenEncoder = new TokenEncoder();

  private String hashedToken;
  private User user;
  private UserToken userToken;

  @Before
  public void setUp() {
    hashedToken = tokenEncoder.encode(TOKEN);
    user = entityManager.persistAndFlush(new User(USER_NAME));
    userToken = entityManager.persistAndFlush(new UserToken(user, hashedToken, TOKEN_ALIAS));
  }

  @Test
  public void givenNotExistingUserName_userTokenNotFound() {
    Optional<UserToken> notExistingUserToken = userTokenRepository.findOneByUserUserNameAndAlias(
        "dummy-user", TOKEN_ALIAS);

    assertThat(notExistingUserToken).isEmpty();
  }

  @Test
  public void givenNotExistingTokenAlias_userTokenNotFound() {
    Optional<UserToken> notExistingUserToken = userTokenRepository.findOneByUserUserNameAndAlias(
        USER_NAME, "dummy-alias");

    assertThat(notExistingUserToken).isEmpty();
  }

  @Test
  public void givenExistingUserNameAndAlias_returnUserToken() {
    Optional<UserToken> existingUserToken = userTokenRepository.findOneByUserUserNameAndAlias(
        USER_NAME, TOKEN_ALIAS);

    assertThat(existingUserToken).isEqualTo(Optional.of(userToken));
  }

  @Test
  public void givenNotExistingHashedToken_userTokenNotFound() {
    Optional<UserToken> notExistingUserToken =
        userTokenRepository.findOneByHashedToken("dummy-token");

    assertThat(notExistingUserToken).isEmpty();
  }

  @Test
  public void givenExistingHashedToken_returnUserToken() {
    Optional<UserToken> existingUserToken =
        userTokenRepository.findOneByHashedToken(hashedToken);

    assertThat(existingUserToken).isEqualTo(Optional.of(userToken));
  }

  @Configuration
  @EnableAutoConfiguration
  @Import(DataTestConfiguration.class)
  static class TestConfiguration {

  }
}
