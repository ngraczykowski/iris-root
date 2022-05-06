package com.silenteight.sep.auth.authentication.basic;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.authorization.AuthorizationProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.validation.Valid;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Profile("basic-auth")
@Configuration
@EnableConfigurationProperties({ BasicAuthProperties.class, AuthorizationProperties.class })
@Slf4j
public class BasicAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

  private final BasicAuthProperties basicAuthProperties;
  private final AccessDeniedHandler restAccessDeniedHandler;
  @Valid
  private final AuthorizationProperties authorizationProperties;

  @Autowired
  BasicAuthenticationConfiguration(
      BasicAuthProperties basicAuthProperties,
      AuthorizationProperties authorizationProperties,
      @Qualifier("restAccessDeniedHandler") AccessDeniedHandler restAccessDeniedHandler) {

    this.basicAuthProperties = basicAuthProperties;
    this.restAccessDeniedHandler = restAccessDeniedHandler;
    this.authorizationProperties = authorizationProperties;
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        // Cross-Site Request Forgery and security Headers disabled for REST API
        .csrf()
        .disable()
        .headers()
        .disable()
            .sessionManagement().sessionCreationPolicy(STATELESS).and()
        .exceptionHandling()
        .accessDeniedHandler(restAccessDeniedHandler)
        .and()
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers(authorizationProperties.getPermitAllUrls()).permitAll()
        .anyRequest().authenticated();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
    PasswordEncoder passwordEncoder = passwordEncoder();
    InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer = authBuilder
        .eraseCredentials(false)
        .inMemoryAuthentication();

    basicAuthProperties
        .getUsers()
        .stream()
        .map(user -> user.getUserDetails(passwordEncoder::encode))
        .forEach(configurer::withUser);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
