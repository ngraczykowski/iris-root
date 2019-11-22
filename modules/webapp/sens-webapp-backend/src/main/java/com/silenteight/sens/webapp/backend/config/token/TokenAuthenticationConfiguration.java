package com.silenteight.sens.webapp.backend.config.token;

import com.silenteight.sens.webapp.users.usertoken.UserTokenConfiguration;
import com.silenteight.sens.webapp.users.usertoken.UserTokenService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@Import(UserTokenConfiguration.class)
class TokenAuthenticationConfiguration {

  @Bean
  public TokenAuthenticationProvider tokenAuthenticationProvider(
      UserTokenService userTokenService) {

    return new TokenAuthenticationProvider(userTokenService);
  }

  @Bean
  SimpleUrlAuthenticationSuccessHandler successHandler() {
    SimpleUrlAuthenticationSuccessHandler successHandler =
        new SimpleUrlAuthenticationSuccessHandler();
    successHandler.setRedirectStrategy(new NoRedirectStrategy());
    return successHandler;
  }

  static class NoRedirectStrategy implements RedirectStrategy {

    @Override
    public void sendRedirect(
        HttpServletRequest request, HttpServletResponse response, String url) {
      // No redirect is required with pure REST
    }
  }
}
