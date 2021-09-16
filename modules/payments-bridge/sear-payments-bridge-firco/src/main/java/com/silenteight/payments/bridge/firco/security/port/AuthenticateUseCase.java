package com.silenteight.payments.bridge.firco.security.port;

import org.springframework.security.core.Authentication;

public interface AuthenticateUseCase {

  /**
   * Authenticates using the provided authentication, unless already authenticated.
   * <p/>
   * Note that this call will throw when authentication fails. The return value is an indicator
   * whether the authentication had to take place.
   * <p/>
   * If the caller was already authenticated by other means (like OAuth2 token), then call to this
   * method is no-op and returns {@literal false}.
   * <p/>
   * If the caller was not authenticated, then the method tries to authenticate with {@link
   * org.springframework.security.authentication.AuthenticationManager}, and returns {@literal true}
   * when successful, otherwise throws.
   *
   * @param authentication
   *     the authentication token to use for authenticating
   *
   * @return true when authenticated, false when already authenticated
   */
  boolean authenticate(Authentication authentication);
}
