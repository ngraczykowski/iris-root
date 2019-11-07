package com.silenteight.sens.webapp.user;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.adapter.user.JpaUserRepository;
import com.silenteight.sens.webapp.common.support.audit.AuditConfiguration;
import com.silenteight.sens.webapp.common.support.audit.AuditReaderProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Import(AuditConfiguration.class)
public class UserServiceConfiguration {

  @Bean
  public UserRepository userRepository(
      JpaUserRepository jpaUserRepository, AuditReaderProvider auditReaderProvider) {

    return new DatabaseUserRepository(jpaUserRepository, auditReaderProvider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserService userService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    return new UserService(userRepository, passwordEncoder);
  }

  @Bean
  UserMappingFinder userMappingFinder(UserRepository userRepository) {
    return new UserMappingFinder(userRepository);
  }
}
