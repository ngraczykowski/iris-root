package com.silenteight.sens.webapp.user;

import com.silenteight.sens.webapp.adapter.user.JpaUserTokenRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(UserServiceConfiguration.class)
public class UserTokenServiceConfiguration {

  @Bean
  UserTokenRepository userTokenRepository(JpaUserTokenRepository jpaUserTokenRepository) {
    return new DatabaseUserTokenRepository(jpaUserTokenRepository);
  }

  @Bean
  UserTokenService userTokenService(
      UserService userService, UserTokenRepository userTokenRepository) {
    return new UserTokenService(userService, userTokenRepository, new TokenEncoder());
  }
}
