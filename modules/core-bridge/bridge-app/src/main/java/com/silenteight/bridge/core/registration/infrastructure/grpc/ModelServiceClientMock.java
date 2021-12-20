package com.silenteight.bridge.core.registration.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.governance.api.library.v1.model.ExportModelOut;
import com.silenteight.governance.api.library.v1.model.FeatureOut;
import com.silenteight.governance.api.library.v1.model.ModelServiceClient;
import com.silenteight.governance.api.library.v1.model.SolvingModelOut;

import java.util.List;

@Slf4j
public class ModelServiceClientMock implements ModelServiceClient {

  @Override
  public SolvingModelOut getSolvingModel() {
    log.info("MOCK: Get solving model");
    return SolvingModelOut.builder()
        .name("solvingModels/mock")
        .policyName("policies/mock")
        .strategyName("strategies/mock")
        .categories(List.of("mockCategory"))
        .features(List.of(FeatureOut.builder()
            .name("feature/mock")
            .agentConfig("agents/mock")
            .build()))
        .build();
  }

  @Override
  public ExportModelOut exportModel(String version) {
    log.info("MOCK: Export model for version {}", version);
    return null;
  }

  @Override
  public String transferModel(byte[] jsonModel) {
    log.info("MOCK: Transfer model");
    return null;
  }

  @Override
  public void sendStatus(String modelName) {
    log.info("MOCK: Send status for model name {}", modelName);
  }
}
