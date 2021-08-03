package com.silenteight.payments.bridge.datasource.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;

import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
class GetMatchLocationInputsUseCase {

  private final FeatureDataAccess featureDataAccess;

  void batchGetMatchLocationInputs(
      BatchGetMatchLocationInputsRequest request,
      Consumer<BatchGetMatchLocationInputsResponse> consumer) {

    readFeatures(
        request, agentInput -> consumer.accept(generateBatchGetMatchLocationInputsResponse(agentInput)));
  }
  private void readFeatures(
      BatchGetMatchLocationInputsRequest request, Consumer<String> consumer) {

//    Pass some required data for the query from the request
    featureDataAccess.streamFeatures(consumer);
  }

  private BatchGetMatchLocationInputsResponse generateBatchGetMatchLocationInputsResponse(
      String agentInput) {
//    TODO: Implement generateBatchGetMatchLocationInputsResponse
    return null;
  }

}
