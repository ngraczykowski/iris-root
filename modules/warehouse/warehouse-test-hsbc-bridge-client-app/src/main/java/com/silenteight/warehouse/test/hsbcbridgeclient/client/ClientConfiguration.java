package com.silenteight.warehouse.test.hsbcbridgeclient.client;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class ClientConfiguration {

  @Value("${test.hsbc-bridge-client.base-url}")
  private String baseUrl;

  WebClient webClient() {
    return WebClient.builder()
        .exchangeStrategies(increaseMaxMemorySize(1024 * 1024 * 1024))
        .baseUrl(baseUrl)
        .filter(logRequest())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  @Bean
  IngestGateway ingestGateway() {
    return new IngestGateway(webClient());
  }

  @Bean
  LearningGateway learningGateway() {
    return new LearningGateway(webClient());
  }

  @Bean
  RecommendGateway recommendGateway() {
    return new RecommendGateway(webClient());
  }

  private static ExchangeFilterFunction logRequest() {
    return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
      log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
      clientRequest
          .headers()
          .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
      return Mono.just(clientRequest);
    });
  }

  private static ExchangeStrategies increaseMaxMemorySize(int size) {
    return ExchangeStrategies.builder()
        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
        .build();
  }
}
