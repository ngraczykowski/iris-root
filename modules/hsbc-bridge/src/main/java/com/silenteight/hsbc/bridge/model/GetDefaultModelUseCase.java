package com.silenteight.hsbc.bridge.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import static com.google.protobuf.Empty.getDefaultInstance;
import static com.silenteight.hsbc.bridge.model.SolvingModelMapper.mapToSolvingModelDto;
import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
class GetDefaultModelUseCase implements ModelUseCase {

  private final SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  public SolvingModelDto getSolvingModel() {
    var solvingModel = solvingModelServiceBlockingStub
        .withDeadlineAfter(deadlineInSeconds, SECONDS)
        .getDefaultSolvingModel(getDefaultInstance());

    return mapToSolvingModelDto(solvingModel);
  }
}
