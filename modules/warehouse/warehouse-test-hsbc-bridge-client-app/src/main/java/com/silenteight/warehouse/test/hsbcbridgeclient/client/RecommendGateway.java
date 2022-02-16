package com.silenteight.warehouse.test.hsbcbridgeclient.client;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class RecommendGateway {

  private static final String BATCH_ID = "batchId";
  private final WebClient webClient;

  @SneakyThrows
  public Mono<String> send(byte[] payload, String batchId) {
    log.info("LearningGateway.send: sending payload: size={}", payload.length);
    return webClient.post()
        .uri("async/batch/v1/{batchId}/recommend", Map.of(BATCH_ID, batchId))
        .bodyValue(new String(payload, StandardCharsets.UTF_8))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode().isError()) {
            return clientResponse.createException().flatMap(Mono::error);
          }
          log.info(
              "RecommendGateway.send: headers={}",
              clientResponse.headers().asHttpHeaders());
          log.info("RecommendGateway.send: statusCode={}", clientResponse.statusCode());
          return clientResponse.bodyToMono(String.class);
        })
        .doOnError(throwable -> log.error("RecommendGateway.send failed.", throwable));
  }

  @SneakyThrows
  public Mono<StatusResponseDto> getStatus(String batchId) {
    log.info("RecommendGateway.status: batchId={}", batchId);

    return webClient.get()
        .uri("async/batch/v1/{batchId}/status", Map.of(BATCH_ID, batchId))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode().isError()) {
            return clientResponse.createException().flatMap(Mono::error);
          }
          log.info(
              "RecommendGateway.status: headers={}",
              clientResponse.headers().asHttpHeaders());
          log.info("RecommendGateway.status: statusCode={}", clientResponse.statusCode());
          return clientResponse.bodyToMono(StatusResponseDto.class);
        })
        .doOnError(throwable -> log.error("RecommendGateway.status failed.", throwable));
  }

  @SneakyThrows
  public Mono<byte[]> getRecommendation(String batchId) {
    log.info("RecommendGateway.getRecommendation: batchId={}", batchId);

    return webClient.get()
        .uri("async/batch/v1/{batchId}/result", Map.of(BATCH_ID, batchId))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode().isError()) {
            return clientResponse.createException().flatMap(Mono::error);
          }
          log.info(
              "RecommendGateway.getRecommendation: headers={}",
              clientResponse.headers().asHttpHeaders());
          log.info(
              "RecommendGateway.getRecommendation: statusCode={}", clientResponse.statusCode());
          return clientResponse.bodyToMono(ByteArrayResource.class);
        })
        .map(ByteArrayResource::getByteArray)
        .doOnError(throwable -> log.error("RecommendGateway.getRecommendation failed.", throwable));
  }
}
