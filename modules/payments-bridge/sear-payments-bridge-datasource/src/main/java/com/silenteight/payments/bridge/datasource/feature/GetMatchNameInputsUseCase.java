package com.silenteight.payments.bridge.datasource.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;

import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
class GetMatchNameInputsUseCase {

  private final FeatureDataAccess featureDataAccess;

  void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request, Consumer<BatchGetMatchNameInputsResponse> consumer) {

    readFeatures(
        request,
        agentInput -> consumer.accept(generateBatchGetMatchNameInputsResponse(agentInput)));
  }

  private void readFeatures(
      BatchGetMatchNameInputsRequest request, Consumer<String> consumer) {

    //Pass some required data for the query from the request
    featureDataAccess.streamFeatures(consumer);
  }

  private BatchGetMatchNameInputsResponse generateBatchGetMatchNameInputsResponse(
      String agentInput) {
    //TODO: Implement generateBatchGetMatchNameInputsResponse
    return null;
  }
}
