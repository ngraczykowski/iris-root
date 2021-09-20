package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchCreateMatchFeaturesUseCase;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchGetFeatureInputUseCase;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
class FeatureAdapter {

  private final BatchGetFeatureInputUseCase getUseCase;
  private final BatchCreateMatchFeaturesUseCase addUseCase;

  BatchCreateAgentInputsResponse batchAgentInputs(BatchCreateAgentInputsRequest request) {
    return addUseCase.batchCreateMatchFeatures(request.getAgentInputsList());
  }

  void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request,
      Consumer<BatchGetMatchNameInputsResponse> onNext) {

    getUseCase.batchGetFeatureInput(
        request.getMatchesList(), request.getFeaturesList(),
        batch -> onNext.accept(batch.castResponse(BatchGetMatchNameInputsResponse.class)));
  }

  void batchGetMatchLocationInputs(
      BatchGetMatchLocationInputsRequest request,
      Consumer<BatchGetMatchLocationInputsResponse> onNext) {

    getUseCase.batchGetFeatureInput(
        request.getMatchesList(), request.getFeaturesList(),
        batch -> onNext.accept(batch.castResponse(BatchGetMatchLocationInputsResponse.class)));
  }
}
