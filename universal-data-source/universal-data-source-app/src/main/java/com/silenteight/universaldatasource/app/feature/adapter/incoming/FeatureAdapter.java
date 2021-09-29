package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.universaldatasource.app.feature.model.BatchFeatureRequest;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchCreateMatchFeaturesUseCase;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchGetFeatureInputUseCase;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import javax.validation.Valid;

@Component
@RequiredArgsConstructor
class FeatureAdapter {

  private static final String LOCATION_FEATURE_INPUT = "LocationFeatureInput";
  private static final String NAME_FEATURE_INPUT = "NameFeatureInput";

  private final BatchGetFeatureInputUseCase getUseCase;
  private final BatchCreateMatchFeaturesUseCase addUseCase;

  BatchCreateAgentInputsResponse batchAgentInputs(
      @Valid BatchCreateAgentInputsRequest request) {
    return addUseCase.batchCreateMatchFeatures(request.getAgentInputsList());
  }

  void batchGetMatchNameInputs(
      @Valid BatchGetMatchNameInputsRequest request,
      Consumer<BatchGetMatchNameInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(NAME_FEATURE_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchNameInputsResponse.class)));
  }

  void batchGetMatchLocationInputs(
      @Valid BatchGetMatchLocationInputsRequest request,
      Consumer<BatchGetMatchLocationInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(LOCATION_FEATURE_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchLocationInputsResponse.class)));
  }
}
