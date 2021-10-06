package com.silenteight.payments.bridge.firco.callback.service;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CallbackRequestProperties.class)
class CallbackRequestConfiguration {

  @Valid
  private final CallbackRequestProperties properties;

  @Bean
  @ConditionalOnProperty(prefix = "pb.cmapi.callback", name = "enabled", havingValue = "true")
  CallbackRequestFactory callbackRequestFactory(RestTemplate restTemplate) {
    return new CallbackRequestFactoryImpl(
        properties.getEndpoint(),
        restTemplate);
  }

  @Bean
  @ConditionalOnProperty(prefix = "pb.cmapi.callback", name = "enabled", havingValue = "false")
  CallbackRequestFactory noCallbackRequestFactory() {
    return new NoCallbackRequestFactoryImpl();
  }

  //@Bean
  //@ConditionalOnProperty(prefix = "pb.cmapi.callback", name = "enabled", havingValue = "true")
  //public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails details) {
  //  OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(details);
  //  oAuth2RestTemplate.getAccessToken();
  //  return oAuth2RestTemplate;
  //}
}
