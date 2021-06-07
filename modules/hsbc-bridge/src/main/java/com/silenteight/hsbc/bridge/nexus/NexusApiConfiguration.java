package com.silenteight.hsbc.bridge.nexus;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.transfer.RepositoryClient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
@EnableConfigurationProperties(NexusApiProperties.class)
@RequiredArgsConstructor
class NexusApiConfiguration {

  private final NexusApiProperties nexusApiProperties;

  @Bean
  RepositoryClient nexusClient() {
    return new NexusModelClient(httpClient(), nexusApiProperties);
  }

  private HttpClient httpClient() {
    return HttpClient.newHttpClient();
  }
}
