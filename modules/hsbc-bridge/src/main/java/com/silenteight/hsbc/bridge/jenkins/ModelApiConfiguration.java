package com.silenteight.hsbc.bridge.jenkins;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.transfer.ModelClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.http.HttpClient;

@Configuration
@EnableConfigurationProperties(JenkinsApiProperties.class)
@RequiredArgsConstructor
class ModelApiConfiguration {

  private final JenkinsApiProperties jenkinsApiProperties;

  @Profile("!modelTransferMock")
  @Bean
  ModelClient jenkinsModelClient() {
    return new JenkinsModelClient(objectMapper(), httpClient(), jenkinsApiProperties);
  }

  @Profile("modelTransferMock")
  @Bean
  ModelClient jenkinsModelClientMock() {
    return new JenkinsModelClientMock();
  }

  private ObjectMapper objectMapper() {
    return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private HttpClient httpClient() {
    return HttpClient.newHttpClient();
  }
}
