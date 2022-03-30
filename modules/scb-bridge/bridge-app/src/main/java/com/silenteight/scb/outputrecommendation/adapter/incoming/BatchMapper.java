package com.silenteight.scb.outputrecommendation.adapter.incoming;

import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.proto.registration.api.v1.MessageBatchError;
import com.silenteight.scb.outputrecommendation.domain.PrepareErrorRecommendationResponseCommand;
import com.silenteight.scb.outputrecommendation.domain.PrepareRecommendationResponseCommand;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
class BatchMapper {

  PrepareRecommendationResponseCommand fromBatchCompletedMessage(
      MessageBatchCompleted batchCompleted) {
    return PrepareRecommendationResponseCommand.builder()
        .batchId(batchCompleted.getBatchId())
        .analysisName(batchCompleted.getAnalysisName())
        // Note, when alertNames is empty, it means all alerts within analysis with analysisName
        .alertNames(List.of())
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