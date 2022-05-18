package com.silenteight.payments.bridge.app.security;

import lombok.RequiredArgsConstructor;

import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, mode = AdviceMode.ASPECTJ)
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor
class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {

  private final SecurityProperties properties;

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


  @Bean
  AccessDecisionManager accessDecisionManager(MethodInterceptor methodSecurityInterceptor) {
    return ((MethodSecurityInterceptor) methodSecurityInterceptor).getAccessDecisionManager();
  }

  @Bean
  GrpcAuthenticationReader grpcAuthenticationReader() {
    return new BasicGrpcAuthenticationReader();
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    var manager = new InMemoryUserDetailsManager();

    // FIXME(ahaczewski): Use proper database-backed credentials store.
    manager.createUser(User.builder()
        .username("login")
        .password(passwordEncoder.encode("password"))
        .roles("USER", "SEND_MESSAGE")
        .build());

    return manager;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    var encoders = Map.of(
        "bcrypt", new BCryptPasswordEncoder(),
        "pbkdf2", new Pbkdf2PasswordEncoder(),
        "scrypt", new SCryptPasswordEncoder(),
        "argon2", new Argon2PasswordEncoder()
    );
    return new DelegatingPasswordEncoder("bcrypt", encoders);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    if (properties.isDisableSecurity()) {
      http.authorizeRequests().anyRequest().permitAll();
    } else {
      http.authorizeRequests()
          .antMatchers("/management/health/**", "/management/prometheus/**", "/status",
              "/mock/cmapi").permitAll()
          .and()
          .authorizeRequests()
          .anyRequest().authenticated();
      http.oauth2ResourceServer(oauth2 -> oauth2.jwt());
    }

    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .sessionFixation().none()
        .and()
        .csrf().disable()
        .formLogin().disable()
        .logout().disable();
  }
}
