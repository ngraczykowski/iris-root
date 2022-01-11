package com.silenteight.warehouse.test.hsbcbridgeclient.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

  @Value("${test.hsbc-bridge-client.base-url}")
  private String baseUrl;

  @Bean
  IngestGateway ingestGateway() {
    WebClient webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    return new IngestGateway(webClient, new ObjectMapper());
  }

  @Bean
  LearningGateway learningGateway() {
    WebClient webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    return new LearningGateway(webClient, new ObjectMapper());
  }
}
