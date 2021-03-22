package com.silenteight.hsbc.bridge.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import com.google.protobuf.Empty;

import static com.silenteight.hsbc.bridge.model.SolvingModelMapper.mapToSolvingModelDto;

@RequiredArgsConstructor
public class GetDefaultModelUseCase implements ModelUseCase {

  private final SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;

  @Override
  public SolvingModelDto getSolvingModel() {
    SolvingModel defaultSolvingModel =
        solvingModelServiceBlockingStub.getDefaultSolvingModel(Empty.getDefaultInstance());
    return mapToSolvingModelDto(defaultSolvingModel);
  }
}
