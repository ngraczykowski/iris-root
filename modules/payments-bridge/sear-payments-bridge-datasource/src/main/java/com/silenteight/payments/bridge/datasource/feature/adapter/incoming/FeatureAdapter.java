package com.silenteight.payments.bridge.datasource.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.BatchGetFeatureInputUseCase;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
class FeatureAdapter {

  private final BatchGetFeatureInputUseCase useCase;

  void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request, Consumer<BatchGetMatchNameInputsResponse> onNext) {

    useCase.batchGetFeatureInput(
        request.getMatchesList(), request.getFeaturesList(),
        batch -> onNext.accept(batch.castResponse(BatchGetMatchNameInputsResponse.class)));
  }

  void batchGetMatchLocationInputs(
      BatchGetMatchLocationInputsRequest request,
      Consumer<BatchGetMatchLocationInputsResponse> onNext) {

    useCase.batchGetFeatureInput(
        request.getMatchesList(), request.getFeaturesList(),
        batch -> onNext.accept(batch.castResponse(BatchGetMatchLocationInputsResponse.class)));
  }
}
