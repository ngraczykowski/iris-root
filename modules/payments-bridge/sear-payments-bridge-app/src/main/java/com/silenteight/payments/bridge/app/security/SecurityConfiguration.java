package com.silenteight.payments.bridge.app.security;

import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.aopalliance.intercept.MethodInterceptor;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
class SecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http
        .csrf().disable()
        .authorizeRequests()
        .anyRequest().authenticated()
        //.antMatchers("/**").permitAll()
        .and()
        .formLogin().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    var authMapper = new SimpleAuthorityMapper();
    authMapper.setConvertToUpperCase(true);

    var authProvider = keycloakAuthenticationProvider();
    authProvider.setGrantedAuthoritiesMapper(authMapper);

    auth.authenticationProvider(authProvider);
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
  public PasswordEncoder passwordEncoder() {
    var encoders = Map.of(
        "bcrypt", new BCryptPasswordEncoder(),
        "pbkdf2", new Pbkdf2PasswordEncoder(),
        "scrypt", new SCryptPasswordEncoder(),
        "argon2", new Argon2PasswordEncoder()
    );
    return new DelegatingPasswordEncoder("bcrypt", encoders);
  }
}
