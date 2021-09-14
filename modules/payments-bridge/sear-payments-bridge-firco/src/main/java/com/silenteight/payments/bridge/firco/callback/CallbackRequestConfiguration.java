package com.silenteight.payments.bridge.firco.callback;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CallbackRequestProperties.class)
class CallbackRequestConfiguration {

  @Valid
  private final CallbackRequestProperties properties;

  @Bean
  @ConditionalOnProperty(prefix = "pb.cmapi.callback", name = "enabled", havingValue = "true")
  CallbackRequestFactory callbackRequestFactory(OAuth2RestTemplate oAuth2RestTemplate) {
    return new CallbackRequestFactoryImpl(
        properties.getEndpoint(),
        oAuth2RestTemplate);
  }

  @Bean
  @ConditionalOnProperty(prefix = "pb.cmapi.callback", name = "enabled", havingValue = "false")
  CallbackRequestFactory noCallbackRequestFactory() {
    return new NoCallbackRequestFactoryImpl();
  }

  @Bean
  @ConditionalOnProperty(prefix = "pb.cmapi.callback", name = "enabled", havingValue = "true")
  public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails details) {
    OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(details);
    oAuth2RestTemplate.getAccessToken();
    return oAuth2RestTemplate;
  }
}
