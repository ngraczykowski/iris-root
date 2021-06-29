package com.silenteight.hsbc.bridge.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.ExportModelResponseDto;
import com.silenteight.hsbc.bridge.model.FeatureDto;
import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.hsbc.bridge.model.SolvingModelDto;

import java.util.List;

@Slf4j
class ModelServiceClientMock implements ModelServiceClient {

  @Override
  public SolvingModelDto getSolvingModel() {
    return SolvingModelDto.builder()
        .name("name")
        .policyName("policy")
        .strategyName("strategy")
        .features(List.of(FeatureDto.builder()
            .name("feature")
            .agentConfig("agentConfig")
            .build()))
        .categories(List.of("category"))
        .build();
  }

  @Override
  public ExportModelResponseDto exportModel(String name) {
    return ExportModelResponseDto.builder().modelJson(name.getBytes()).build();
  }

  @Override
  public void transferModel(byte[] model) {
    log.info("Transfer mocked model started ...");
  }

  @Override
  public void sendStatus(String modelName) {
    log.info("Transfer Governance model: {} has status: MOCK", modelName);
  }
}
