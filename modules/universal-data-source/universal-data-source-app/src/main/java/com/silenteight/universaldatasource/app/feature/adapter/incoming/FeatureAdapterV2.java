package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.historicaldecisions.v2.BatchGetMatchHistoricalDecisionsInputsRequest;
import com.silenteight.datasource.api.historicaldecisions.v2.BatchGetMatchHistoricalDecisionsInputsResponse;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsRequest;
import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsResponse;
import com.silenteight.datasource.api.ispep.v2.IsPepFeatureInput;
import com.silenteight.universaldatasource.app.feature.model.BatchFeatureRequest;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchGetFeatureInputUseCase;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import javax.validation.Valid;

@Component
@RequiredArgsConstructor
class FeatureAdapterV2 {

  private static final String HISTORICAL_DECISIONS_INPUT_V2 =
      HistoricalDecisionsFeatureInput.class.getCanonicalName();
  private static final String IS_PEP_INPUT_V2 = IsPepFeatureInput.class.getCanonicalName();

  private final BatchGetFeatureInputUseCase getUseCase;

  public void batchGetMatchHistoricalDecisionsInputs(
      @Valid BatchGetMatchHistoricalDecisionsInputsRequest request,
      Consumer<BatchGetMatchHistoricalDecisionsInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(HISTORICAL_DECISIONS_INPUT_V2)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(
            batch.castResponse(BatchGetMatchHistoricalDecisionsInputsResponse.class)));
  }

  public void batchGetMatchIsPepInputs(
      @Valid BatchGetMatchIsPepInputsRequest request,
      Consumer<BatchGetMatchIsPepInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(IS_PEP_INPUT_V2)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchIsPepInputsResponse.class)));
  }
}
