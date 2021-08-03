package com.silenteight.payments.bridge.datasource.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;

import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class FeatureFacade {

  private final CreateFeatureUseCase createFeatureUseCase;

  private final GetMatchNameInputsUseCase getMatchNameInputsUseCase;

  private final GetMatchLocationInputsUseCase getMatchLocationInputsUseCase;

  public void saveFeature(Object featureObject) {
    createFeatureUseCase.createFeature(featureObject);
  }

  public void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request, Consumer<BatchGetMatchNameInputsResponse> consumer) {

    getMatchNameInputsUseCase.batchGetMatchNameInputs(request, consumer);
  }

  public void batchGetMatchLocationInputs(
      BatchGetMatchLocationInputsRequest request,
      Consumer<BatchGetMatchLocationInputsResponse> consumer) {

    getMatchLocationInputsUseCase.batchGetMatchLocationInputs(request, consumer);
  }
}
