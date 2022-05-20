package com.silenteight.hsbc.bridge.nexus;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.transfer.RepositoryClient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.http.HttpClient;

@Configuration
@EnableConfigurationProperties(NexusApiProperties.class)
@RequiredArgsConstructor
class NexusApiConfiguration {

  private final NexusApiProperties nexusApiProperties;

  @Profile("!modelTransferMock")
  @Bean
  RepositoryClient nexusClient() {
    return new NexusModelClient(httpClient(), nexusApiProperties);
  }

  @Profile("modelTransferMock")
  @Bean
  RepositoryClient nexusClientMock() {
    return new NexusModelClientMock();
  }

  private HttpClient httpClient() {
    return HttpClient.newHttpClient();
  }
}
