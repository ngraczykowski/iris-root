package com.silenteight.payments.bridge.datasource.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.payments.bridge.datasource.feature.FeatureFacade;

import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
class FeatureService {

  private final FeatureFacade facade;

  void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request, Consumer<BatchGetMatchNameInputsResponse> onNext) {
    facade.batchGetMatchNameInputs(request, onNext);
  }

  void batchGetMatchLocationInputs(
      BatchGetMatchLocationInputsRequest request, Consumer<BatchGetMatchLocationInputsResponse> onNext) {
    facade.batchGetMatchLocationInputs(request, onNext);
  }


}
