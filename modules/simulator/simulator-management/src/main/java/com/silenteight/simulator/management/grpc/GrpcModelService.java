package com.silenteight.simulator.management.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.ModelRequest;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;
import com.silenteight.simulator.management.create.ModelService;

@Slf4j
@RequiredArgsConstructor
class GrpcModelService implements ModelService {

  @NonNull
  private final SolvingModelServiceBlockingStub solvingModelStub;

  @Override
  public SolvingModel getModel(String model) {
    log.trace("Getting SolvingModel by name={}", model);

    return solvingModelStub.getSolvingModel(toGrpcRequest(model));
  }

  private static ModelRequest toGrpcRequest(String model) {
    return ModelRequest.newBuilder()
        .setModel(model)
        .build();
  }
}
