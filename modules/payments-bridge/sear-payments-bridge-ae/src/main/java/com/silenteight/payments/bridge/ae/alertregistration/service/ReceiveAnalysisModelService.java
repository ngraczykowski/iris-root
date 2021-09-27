package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.ReceiveAnalysisModelUseCase;
import com.silenteight.payments.bridge.governance.core.solvingmodel.port.CurrentModelClientPort;

import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class ReceiveAnalysisModelService implements ReceiveAnalysisModelUseCase {

  private final CurrentModelClientPort getCurrentModelPort;

  public CreateAnalysisRequest createAnalysisRequest() {
    var model = getCurrentModelPort.getCurrentModel();
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
