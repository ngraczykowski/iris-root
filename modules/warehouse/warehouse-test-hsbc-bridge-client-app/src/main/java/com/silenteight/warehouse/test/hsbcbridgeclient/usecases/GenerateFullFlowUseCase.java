package com.silenteight.warehouse.test.hsbcbridgeclient.usecases;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.hsbcbridgeclient.client.IngestGateway;
import com.silenteight.warehouse.test.hsbcbridgeclient.client.LearningGateway;
import com.silenteight.warehouse.test.hsbcbridgeclient.client.RecommendGateway;

import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
public class GenerateFullFlowUseCase {

  private final BatchGeneratorService batchGeneratorService;

  private final IngestGateway ingestGateway;

  private final RecommendGateway recommendGateway;

  private final LearningGateway learningGateway;

  @SneakyThrows
  @Scheduled(cron = "${test.hsbc-bridge-client.cron}")
  // If you prefer to generate at startup, replace the line above with the line below:
  // @EventListener(ApplicationReadyEvent.class)
  public void activate() {
    Batch newBatch = batchGeneratorService.generateBatch();
    recommendGateway.send(newBatch.getPayload(), newBatch.getBatchId()).block();

    try {
      Batch batch = Flux.interval(Duration.ofSeconds(5))
          .flatMap(counter -> recommendGateway.getStatus(newBatch.getBatchId()))
          .map(status -> Batch.builder()
              .batchId(status.getBatchId())
              .status(status.getBatchStatus())
              .payload(newBatch.getPayload())
              .build())
          .filter(Batch::isCompleted)
          .timeout(Duration.ofSeconds(10000))
          .next()
          .flatMap(this::getRecommendation)
          .block();

      sendIngest(batch).block();

      sendLearning(batch).block();

      log.info("Processing completed, batchId={}", batch.getBatchId());
    } catch (RuntimeException e) {
      log.error("Processing failed, batchId={}", newBatch.getBatchId());
    }
  }

  private Mono<Batch> getRecommendation(Batch batch) {
    return recommendGateway.getRecommendation(batch.getBatchId())
        .map(ingest -> batch.toBuilder()
            .ingest(ingest)
            .build());
  }

  private Mono<String> sendIngest(Batch batch) {
    return ingestGateway.send(batch.getIngest());
  }

  private Mono<String> sendLearning(Batch batch) {
    return learningGateway.send(batch.getPayload(), batch.getBatchId());
  }
}
