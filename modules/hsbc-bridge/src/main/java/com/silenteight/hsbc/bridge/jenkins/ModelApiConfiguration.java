package com.silenteight.hsbc.bridge.jenkins;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.transfer.ModelClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
@EnableConfigurationProperties(JenkinsApiProperties.class)
@RequiredArgsConstructor
class ModelApiConfiguration {

  private final JenkinsApiProperties jenkinsApiProperties;

  @Bean
  ModelClient jenkinsModelClient() {
    return new JenkinsModelClient(objectMapper(), httpClient(), jenkinsApiProperties);
  }

  private ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  private HttpClient httpClient() {
    return HttpClient.newHttpClient();
  }
}
