package com.silenteight.warehouse.test.hsbcbridgeclient.client;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LearningGateway {

  private final WebClient webClient;

  @SneakyThrows
  public Mono<String> send(byte[] payload, String batchId) {
    log.info("LearningGateway: sending payload: size={}", payload.length);

    return webClient.post()
        .uri("async/batch/v1/{batchId}-learning/learning", Map.of("batchId", batchId))
        .bodyValue(new String(payload, StandardCharsets.UTF_8))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode().isError()) {
            return clientResponse.createException().flatMap(Mono::error);
          }

          log.info("LearningGateway: headers={}", clientResponse.headers().asHttpHeaders());
          log.info("LearningGateway: statusCode={}", clientResponse.statusCode());
          return clientResponse.bodyToMono(String.class);
        })
        .doOnError(throwable -> log.error("LearningGateway failed.", throwable));
  }
}
