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
  public void updateWithPreProcessedStatus(@NonNull String bulkId) {
    var bulk = bulkRepository.findById(bulkId);

    bulk.setStatus(PRE_PROCESSED);
    bulkRepository.save(bulk);
  }
}
