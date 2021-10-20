package com.silenteight.payments.bridge.app.adapter.solvingmodel;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.BuildCreateAnalysisRequestPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.CreateAnalysisUseCase;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.ModelUpdatedEvent;
import com.silenteight.payments.bridge.governance.solvingmodel.model.AnalysisModel;
import com.silenteight.payments.bridge.governance.solvingmodel.port.GetCurrentProductionModelUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@MessageEndpoint
class SolvingModelAdapter implements BuildCreateAnalysisRequestPort {

  private final GetCurrentProductionModelUseCase getCurrentProductionModelUseCase;
  private final CreateAnalysisUseCase createAnalysisUseCase;

  @ServiceActivator(inputChannel = CommonChannels.NEW_MODEL_RECEIVED)
  void analysisModelUpdated(ModelUpdatedEvent event) {
    var model = event.getData(AnalysisModel.class);

    createAnalysisUseCase.createAnalysis(buildRequest(model));
  }

  @Override
  public CreateAnalysisRequest build() {
    var model = getCurrentProductionModelUseCase.getModel();

    return buildRequest(model);
  }

  @Nonnull
  private CreateAnalysisRequest buildRequest(AnalysisModel model) {
    return CreateAnalysisRequest
        .newBuilder()
        .setAnalysis(Analysis
            .newBuilder()
            .addAllCategories(model.getCategories())
            .addAllFeatures(model
                .getFeatures()
                .stream()
                .map(f -> Feature
                    .newBuilder()
                    .setFeature(f.getName())
                    .setAgentConfig(f.getAgentConfig())
                    .build())
                .collect(
                    toList()))
            .setPolicy(model.getPolicyName())
            .setStrategy(model.getStrategyName())
            .build())
        .build();
  }
}
