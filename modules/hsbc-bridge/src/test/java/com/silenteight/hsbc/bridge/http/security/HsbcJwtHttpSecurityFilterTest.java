package com.silenteight.hsbc.bridge.http.security;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.AuthenticationException;

import static org.assertj.core.api.Assertions.*;

class HsbcJwtHttpSecurityFilterTest {

  private final HsbcJwtHttpSecurityFilter underTest = new HsbcJwtHttpSecurityFilter();

  @Test
  void givenNoExpectedHeader_throwsException() {
    var request = new MockHttpServletRequest();

    ThrowingCallable when = () -> underTest.attemptAuthentication(request, null);

    assertThatThrownBy(when).isInstanceOf(AuthenticationException.class);
  }

  @Test
  void givenCorrectHeaderWithNoBearer_throwsException() {
    var request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Some value");

    ThrowingCallable when = () -> underTest.attemptAuthentication(request, null);

    assertThatThrownBy(when).isInstanceOf(AuthenticationException.class);
  }

  @Test
  void givenCorrectHeaderWithBearer_returnCorrectAuthenticationObject() {
    underTest.setAuthenticationManager(auth -> auth);
    var request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer token");

    var actual = underTest.attemptAuthentication(request, null);

    assertThat(actual.getCredentials()).isEqualTo("token");
  }
}
