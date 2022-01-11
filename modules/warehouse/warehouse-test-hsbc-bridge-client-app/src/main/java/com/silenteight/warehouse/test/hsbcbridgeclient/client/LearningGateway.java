package com.silenteight.warehouse.test.hsbcbridgeclient.client;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LearningGateway {

  private final WebClient webClient;

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public Mono<String> send(JsonNode jsonNode, String batchId) {
    byte[] payload = objectMapper.writeValueAsBytes(jsonNode);

    log.info("LearningGateway: sending payload: size={}", payload.length);

    return webClient.post()
        .uri("async/batch/v1/{batchId}/learning", Map.of("batchId", batchId))
        .bodyValue(payload)
        .exchangeToMono(clientResponse -> {
          log.info("LearningGateway: headers={}", clientResponse.headers());
          log.info("LearningGateway: statusCode={}", clientResponse.statusCode());
          return clientResponse.bodyToMono(String.class);
        })
        .doOnError(throwable -> log.error("LearningGateway failed.", throwable));
  }
}
