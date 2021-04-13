package com.silenteight.hsbc.bridge.http.security;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true
)
@Slf4j
@EnableConfigurationProperties(HsbcSecurityConfigurationProperties.class)
class WebSecurityConfiguration {

  @Bean
  @ConditionalOnProperty(value = "silenteight.bridge.security", havingValue = "true")
  WebSecurityConfigurerAdapter secured(HsbcSecurityConfigurationProperties properties) {
    log.info("HSBC JWT web security enabled");
    return new HsbcWebSecurityConfigurerAdapter(properties);
  }

  @Bean
  @ConditionalOnProperty(value = "silenteight.bridge.security", havingValue = "true")
  AuthenticationManager authenticationManager(
      WebSecurityConfigurerAdapter webSecurityConfigurerAdapter) throws Exception {
    return webSecurityConfigurerAdapter.authenticationManagerBean();
  }

  @Bean
  @ConditionalOnProperty(value = "silenteight.bridge.security", havingValue = "false", matchIfMissing = true)
  WebSecurityConfigurerAdapter unSecured() {
    log.info("HSBC JWT web security disabled");

    return new DisabledWebSecurityConfigurerAdapter();
  }
}
