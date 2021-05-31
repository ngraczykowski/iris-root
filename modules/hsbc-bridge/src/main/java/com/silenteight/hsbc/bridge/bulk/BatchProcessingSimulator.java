package com.silenteight.hsbc.bridge.bulk;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.recommendation.RecommendationGenerator;
import com.silenteight.hsbc.bridge.recommendation.RecommendationGenerator.GenerationRequest;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PROCESSING;

/**
 * DEV ONLY
 */
@RequiredArgsConstructor
class BatchProcessingSimulator {

  private final BulkRepository bulkRepository;
  private final RecommendationGenerator generator;

  @Scheduled(fixedRateString = "PT30S" , initialDelay = 2000)
  @Transactional
  public void simulate() {
    findBatchInProcessing().ifPresent(b -> {

      var alerts = b.getValidAlerts().stream()
          .map(BulkAlertEntity::getName)
          .collect(Collectors.toList());
      var analysis = b.getAnalysis();

      generator.generate(new GenerationRequest() {

        @Override
        public String getAnalysis() {
          return analysis.getName();
        }

        @Override
        public Collection<String> getAlerts() {
          return alerts;
        }
      });
    });
  }

  private Optional<Bulk> findBatchInProcessing() {
    return bulkRepository.findByStatus(PROCESSING).stream().findFirst();
  }
}
