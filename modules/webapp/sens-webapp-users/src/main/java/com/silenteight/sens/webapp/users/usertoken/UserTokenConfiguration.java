package com.silenteight.sens.webapp.users.usertoken;

import com.silenteight.sens.webapp.users.user.UserService;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
public class UserTokenConfiguration {

  @Bean
  UserTokenService userTokenService(
      UserService userService, UserTokenRepository userTokenRepository) {

    return new UserTokenService(userService, userTokenRepository, new TokenEncoder());
  }
}
