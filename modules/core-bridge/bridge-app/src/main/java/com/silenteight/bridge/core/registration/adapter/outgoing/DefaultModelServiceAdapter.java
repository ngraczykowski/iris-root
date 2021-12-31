package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModel;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelFeature;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelService;
import com.silenteight.governance.api.library.v1.model.FeatureOut;
import com.silenteight.governance.api.library.v1.model.GovernanceLibraryRuntimeException;
import com.silenteight.governance.api.library.v1.model.ModelServiceClient;
import com.silenteight.governance.api.library.v1.model.SolvingModelOut;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class DefaultModelServiceAdapter implements DefaultModelService {

  private final ModelServiceClient modelServiceClient;

  @Override
  @Retryable(GovernanceLibraryRuntimeException.class)
  public DefaultModel getForSolving() {
    var solvingModel = modelServiceClient.getSolvingModel();
    return createDefaultModel(solvingModel);
  }

  private DefaultModel createDefaultModel(SolvingModelOut solvingModel) {
    return DefaultModel.builder()
        .name(solvingModel.getName())
        .policyName(solvingModel.getPolicyName())
        .strategyName(solvingModel.getStrategyName())
        .features(createFeatures(solvingModel.getFeatures()))
        .categories(solvingModel.getCategories())
        .build();
  }

  private List<DefaultModelFeature> createFeatures(List<FeatureOut> features) {
    return features.stream()
        .map(this::createFeature)
        .toList();
  }

  private DefaultModelFeature createFeature(FeatureOut feature) {
    return DefaultModelFeature.builder()
        .name(feature.getName())
        .agentConfig(feature.getAgentConfig())
        .build();
  }
}
