package com.silenteight.payments.bridge.app.rest;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HttpClientProperties.class)
@RequiredArgsConstructor
public class RestTemplateBuilderConfiguration {

  private final HttpClientProperties properties;

  @Bean
  public RestTemplateBuilder restTemplateBuilder(
      RestTemplateBuilderConfigurer restTemplateBuilderConfigurer) {

    var builder = new RestTemplateBuilder();

    return restTemplateBuilderConfigurer
        .configure(builder)
        .detectRequestFactory(false)
        .requestFactory(properties::createRequestFactory);
  }
}
