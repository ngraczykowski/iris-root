package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.*;

@RequiredArgsConstructor
class BulkUpdater {

  private final BulkRepository bulkRepository;

  @Transactional
  public void updateWithAnalysisTimeout(long analysisId) {
    bulkRepository.findByAnalysisId(analysisId)
        .ifPresent(bulk -> {
          bulk.error("Analysis timeout exception");
          bulkRepository.save(bulk);
        });
  }

  @Transactional
  public void updateWithCompletedStatus(@NonNull String analysis) {
    bulkRepository.findByAnalysisName(analysis).ifPresent(bulk -> {
      bulk.setStatus(COMPLETED);
      bulkRepository.save(bulk);
    });
  }

  @Transactional
  public void updateWithPreProcessedStatus(@NonNull String bulkId) {
    bulkRepository.findById(bulkId).ifPresent(b -> {
      b.setStatus(PRE_PROCESSED);
      bulkRepository.save(b);
    });
  }

  @Transactional
  public void updateWithUnavailableRecommendation(String analysis) {
    bulkRepository.findByAnalysisName(analysis).ifPresent(bulk -> {
      bulk.setStatus(ERROR);
      bulkRepository.save(bulk);
    });
  }
}
