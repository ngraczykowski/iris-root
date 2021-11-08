package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
class BulkUpdater {

  private final BulkRepository bulkRepository;

  @Transactional
  public void updateWithAnalysisTimeout(long analysisId) {
    bulkRepository.findByAnalysisId(analysisId)
        .ifPresent(bulk -> {
          bulk.error("Analysis timeout exception");
          bulkRepository.save(bulk);
          log.error("Set batch with id:{} status ERROR", bulk.getId());
        });
  }

  @Transactional
  public void updateWithCompletedStatus(@NonNull String analysis) {
    bulkRepository.findByAnalysisName(analysis).ifPresent(bulk -> {
      bulk.setStatus(BulkStatus.COMPLETED);
      bulkRepository.save(bulk);
      log.info("Solving batch with id:{} has been completed", bulk.getId());
    });
  }

  @Transactional
  public void updateWithPreProcessedStatus(@NonNull String bulkId) {
    bulkRepository.findById(bulkId).ifPresent(b -> {
      b.setStatus(BulkStatus.PRE_PROCESSED);
      bulkRepository.save(b);
      log.info("Set batch with id:{} status PRE_PROCESSED", bulkId);
    });
  }

  @Transactional
  public void updateWithPreProcessingStatus(@NonNull String bulkId) {
    bulkRepository.findById(bulkId).ifPresent(b -> {
      b.setStatus(BulkStatus.PRE_PROCESSING);
      bulkRepository.save(b);
      log.info("Set batch with id:{} status PRE_PROCESSING", bulkId);
    });
  }

  @Transactional
  public void updateWithUnavailableRecommendation(String analysis) {
    bulkRepository.findByAnalysisName(analysis).ifPresent(bulk -> {
      bulk.setStatus(BulkStatus.ERROR);
      bulkRepository.save(bulk);
      log.error("Set batch with id:{} status ERROR", bulk.getId());
    });
  }
}
