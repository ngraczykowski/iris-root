/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchStatus;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutputRecommendationFacade {

  private final RecommendationsProcessor recommendationsProcessor;
  private final BatchInfoService batchInfoService;

  public void prepareCompletedBatchRecommendations(PrepareRecommendationResponseCommand command) {
    recommendationsProcessor.processBatchCompleted(command);
    batchInfoService.changeStatus(command.batchId(), BatchStatus.COMPLETED);
  }

  public void prepareErrorBatchRecommendations(PrepareErrorRecommendationResponseCommand command) {
    recommendationsProcessor.processBatchError(command);
    batchInfoService.changeStatus(command.batchId(), BatchStatus.ERROR);
  }
}
