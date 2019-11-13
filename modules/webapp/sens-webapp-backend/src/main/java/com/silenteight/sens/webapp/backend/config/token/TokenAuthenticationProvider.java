package com.silenteight.sens.webapp.backend.config.token;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.domain.user.UserToken;
import com.silenteight.sens.webapp.kernel.security.SensUserDetails;
import com.silenteight.sens.webapp.user.UserTokenService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(access = PACKAGE)
public final class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  private final UserTokenService userTokenService;

  @Override
  protected void additionalAuthenticationChecks(
      UserDetails userDetails, UsernamePasswordAuthenticationToken auth) {
    // nothing to do
  }

  @Override
  protected UserDetails retrieveUser(
      String userName, UsernamePasswordAuthenticationToken authentication) {

    Object token = authentication.getCredentials();
    Optional<UserToken> userToken = userTokenService.find(token.toString());

    return userToken
        .map(UserToken::getUser)
        .map(TokenAuthenticationProvider::createUserDetails)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Cannot find user with authentication token=" + token));
  }

  private static UserDetails createUserDetails(User user) {
    return SensUserDetails
        .builder()
        .userId(user.getId())
        .username(user.getUserName())
        .authorities(user.getAuthorities())
        .superUser(user.isSuperUser())
        .build();
  }
}
