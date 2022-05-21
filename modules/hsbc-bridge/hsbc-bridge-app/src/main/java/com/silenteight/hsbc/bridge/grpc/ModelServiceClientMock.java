package com.silenteight.hsbc.bridge.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.hsbc.bridge.model.dto.ExportModelResponseDto;
import com.silenteight.hsbc.bridge.model.dto.FeatureDto;
import com.silenteight.hsbc.bridge.model.dto.ImportNewModelResponseDto;
import com.silenteight.hsbc.bridge.model.dto.SolvingModelDto;

import java.util.Arrays;
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
  public String transferModel(byte[] jsonModel) {
    var model =
        ImportNewModelResponseDto.builder().model(Arrays.toString(jsonModel)).build().getModel();
    log.info("Imported model: {} is used", model);
    return model;
  }

  @Override
  public void sendStatus(String version) {
    log.info("Transfer Governance model: {} has status: MOCK", version);
  }
}
