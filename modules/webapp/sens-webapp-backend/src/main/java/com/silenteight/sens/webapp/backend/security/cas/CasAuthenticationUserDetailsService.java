package com.silenteight.sens.webapp.backend.security.cas;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.kernel.security.SensUserDetails;
import com.silenteight.sens.webapp.users.user.User;
import com.silenteight.sens.webapp.users.user.UserService;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@AllArgsConstructor
public class CasAuthenticationUserDetailsService extends AbstractCasAssertionUserDetailsService {

  private final UserService userService;

  @Override
  protected UserDetails loadUserDetails(@NonNull Assertion assertion) {
    return new CasUserBuilder(assertion).build();
  }

  @RequiredArgsConstructor
  private class CasUserBuilder {

    @NonNull
    private final Assertion assertion;

    SensUserDetails build() {
      Optional<User> userByName = userService.getUserByName(getUserName());
      if (userByName.isPresent())
        return getPresentUserDetails(userByName.get());
      else
        return getUserDetailsWithoutAuthorities();
    }

    private SensUserDetails getPresentUserDetails(User user) {
      return SensUserDetails
          .builder()
          .userId(user.getId())
          .username(user.getUserName())
          .authorities(user.getAuthorities())
          .superUser(user.isSuperUser())
          .displayName(user.getDisplayName())
          .build();
    }

    private SensUserDetails getUserDetailsWithoutAuthorities() {
      return SensUserDetails
          .builder()
          .username(getUserName())
          .build();
    }

    private String getUserName() {
      return assertion.getPrincipal().getName();
    }
  }
}
