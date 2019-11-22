package com.silenteight.sens.webapp.users.user;

import com.silenteight.sens.webapp.common.support.audit.AuditConfiguration;
import com.silenteight.sens.webapp.common.support.audit.AuditReaderProvider;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EntityScan
@EnableJpaRepositories
@Import(AuditConfiguration.class)
public class UserConfiguration {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserService userService(
      PasswordEncoder passwordEncoder,
      UserRepository userRepository,
      AuditReaderProvider auditReaderProvider) {

    return new UserService(userRepository, passwordEncoder, auditReaderProvider);
  }

  @Bean
  UserMappingFinder userMappingFinder(UserRepository userRepository) {
    return new UserMappingFinder(userRepository);
  }
}
