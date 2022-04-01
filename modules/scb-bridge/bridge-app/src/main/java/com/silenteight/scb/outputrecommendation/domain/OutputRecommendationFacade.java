package com.silenteight.scb.outputrecommendation.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;

import org.springframework.stereotype.Service;

import static com.silenteight.scb.ingest.domain.model.BatchStatus.COMPLETED;
import static com.silenteight.scb.ingest.domain.model.BatchStatus.ERROR;

@Service
@RequiredArgsConstructor
public class OutputRecommendationFacade {

  private final RecommendationsProcessor recommendationsProcessor;
  private final BatchInfoService batchInfoService;

  public void prepareCompletedBatchRecommendations(PrepareRecommendationResponseCommand command) {
    recommendationsProcessor.processBatchCompleted(command);
    batchInfoService.changeStatus(command.batchId(), COMPLETED);
  }

  public void prepareErrorBatchRecommendations(PrepareErrorRecommendationResponseCommand command) {
    recommendationsProcessor.processBatchError(command);
    batchInfoService.changeStatus(command.batchId(), ERROR);
  }
}
