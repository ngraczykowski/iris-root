package com.silenteight.warehouse.test.hsbcbridgeclient.client;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class IngestGateway {

  private final WebClient webClient;

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public Mono<String> send(JsonNode jsonNode) {
    byte[] payload = objectMapper.writeValueAsBytes(jsonNode);

    log.info("IngestGateway: sending payload: size={}, {}", payload.length);

    return webClient.post()
        .uri("async/batch/v1/ingestRecommendations")
        .bodyValue(payload)
        .exchangeToMono(clientResponse -> {
          log.info("IngestGateway: headers={}", clientResponse.headers());
          log.info("IngestGateway: statusCode={}", clientResponse.statusCode());
          return clientResponse.bodyToMono(String.class);
        })
        .doOnError(throwable -> log.error("IngestGateway failed.", throwable));
  }
}
