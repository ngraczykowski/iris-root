package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
class BulkUpdater {

  private final BulkRepository bulkRepository;

  @Transactional
  public void updateWithAnalysisTimeout(long analysisId) {
    bulkRepository.findByAnalysisId(analysisId)
        .ifPresent(bulk -> bulk.error("Analysis timeout exception"));
  }
}
