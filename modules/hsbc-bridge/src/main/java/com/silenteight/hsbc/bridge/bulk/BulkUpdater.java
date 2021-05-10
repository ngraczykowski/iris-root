package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
class BulkUpdater {

  private final BulkRepository bulkRepository;

  @Transactional
  public void updateWithError(@NonNull String id, String errorMessage) {
    var bulk = bulkRepository.findById(id);
    bulk.error(errorMessage);
    bulkRepository.save(bulk);
  }

  @Transactional
  public void updateWithAnalysis(@NonNull String id, long analysisId) {
    var bulk = bulkRepository.findById(id);
    bulk.setAnalysisId(analysisId);
    bulkRepository.save(bulk);
  }

  @Transactional
  public void updateWithAnalysisTimeout(long analysisId) {
    bulkRepository.findByAnalysisId(analysisId)
        .ifPresent(bulk -> bulk.error("Analysis timeout exception"));
  }
}
