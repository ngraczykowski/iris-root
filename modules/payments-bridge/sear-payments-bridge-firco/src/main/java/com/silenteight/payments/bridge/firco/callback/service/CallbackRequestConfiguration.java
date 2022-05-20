package com.silenteight.payments.bridge.firco.callback.service;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CallbackRequestProperties.class)
class CallbackRequestConfiguration {

  @Valid
  private final CallbackRequestProperties properties;

  @Bean
  @ConditionalOnProperty(prefix = "pb.cmapi.callback", name = "enabled", havingValue = "true")
  CallbackRequestFactory callbackRequestFactory(
      OAuth2AuthorizedClientManager manager, RestTemplateBuilder restTemplateBuilder) {

    var interceptor = getOAuth2Interceptor(manager);
    var restTemplate = restTemplateBuilder.additionalInterceptors(interceptor)
        .setConnectTimeout(properties.getConnectionTimeout())
        .setReadTimeout(properties.getReadTimeout())
        .build();

    return new HttpCallbackRequestFactory(properties.getEndpoint(), restTemplate);
  }

  private OAuth2AuthorizedClientInterceptor getOAuth2Interceptor(
      OAuth2AuthorizedClientManager manager) {

    var key = RandomStringUtils.randomAlphanumeric(16);
    var principal = RandomStringUtils.randomAlphanumeric(16);

    return OAuth2AuthorizedClientInterceptor.builder()
        .manager(manager)
        .clientRegistrationId(properties.getClientRegistrationId())
        .principalSupplier(() -> new AnonymousAuthenticationToken(
            key, principal, AuthorityUtils.createAuthorityList("ANONYMOUS")))
        .build();
  }

  @Bean
  @ConditionalOnProperty(prefix = "pb.cmapi.callback", name = "enabled", havingValue = "false",
      matchIfMissing = true)
  CallbackRequestFactory noCallbackRequestFactory() {
    return new NoopCallbackRequestFactory();
  }
}
