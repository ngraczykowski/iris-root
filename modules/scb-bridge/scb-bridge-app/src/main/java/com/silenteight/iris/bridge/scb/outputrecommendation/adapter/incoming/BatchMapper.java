/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.incoming;

import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.proto.registration.api.v1.MessageBatchError;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.PrepareErrorRecommendationResponseCommand;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.PrepareRecommendationResponseCommand;

import org.springframework.stereotype.Component;

@Component
class BatchMapper {

  PrepareRecommendationResponseCommand fromBatchCompletedMessage(
      MessageBatchCompleted batchCompleted) {
    return PrepareRecommendationResponseCommand.builder()
        .batchId(batchCompleted.getBatchId())
        .analysisName(batchCompleted.getAnalysisName())
        .batchMetadata(batchCompleted.getBatchMetadata())
        .build();
  }

  PrepareErrorRecommendationResponseCommand fromBatchErrorMessage(
      MessageBatchError batchError) {
    return PrepareErrorRecommendationResponseCommand.builder()
        .batchId(batchError.getBatchId())
        .batchMetadata(batchError.getBatchMetadata())
        .errorDescription(batchError.getErrorDescription())
        .build();
  }
}
