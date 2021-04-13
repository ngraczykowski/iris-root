package com.silenteight.hsbc.bridge.auth;

import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

class HsbcJwtAuthorizationProviderTest {

  private final AuthenticationProvider underTest =
      new HsbcAuthorizationProviderConfigurer().create();

  @Test
  void supportsCorrectToken() {
    assertThat(underTest.supports(HsbcJwtAuthenticationToken.class)).isTrue();
  }

  @Test
  void givenCorrectToken_authenticates() {
    var given = HsbcJwtAuthenticationToken.unauthenticated(Fixtures.CORRECT_TOKEN);

    var actual = underTest.authenticate(given);

    assertThat(actual.isAuthenticated()).isTrue();
  }

  @Test
  void givenExpiredToken_throwsException() {
    var given = HsbcJwtAuthenticationToken.unauthenticated(Fixtures.EXPIRED_TOKEN);

    ThrowingCallable when = () -> underTest.authenticate(given);

    assertThatThrownBy(when).isInstanceOf(AuthenticationException.class);
  }

  @Test
  void givenNonTokenString_throwsException() {
    var given = HsbcJwtAuthenticationToken.unauthenticated(Fixtures.NON_TOKEN);

    ThrowingCallable when = () -> underTest.authenticate(given);

    assertThatThrownBy(when).isInstanceOf(AuthenticationException.class);
  }

  private static class Fixtures {

    private static final String CORRECT_TOKEN = Jwts.builder()
        .setExpiration(Date.from(Instant.now().plusSeconds(60)))
        .compact();

    private static final String EXPIRED_TOKEN = Jwts.builder()
        .setExpiration(Date.from(Instant.now().minusSeconds(60)))
        .compact();

    private static final String NON_TOKEN = RandomStringUtils.random(300);
  }
}
