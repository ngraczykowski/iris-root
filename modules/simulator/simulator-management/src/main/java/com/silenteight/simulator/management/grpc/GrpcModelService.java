package com.silenteight.simulator.management.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.ModelRequest;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;
import com.silenteight.simulator.management.ModelService;

@RequiredArgsConstructor
class GrpcModelService implements ModelService {

  @NonNull
  private final SolvingModelServiceBlockingStub solvingModelStub;

  @Override
  public SolvingModel getModel(String model) {
    return solvingModelStub.getSolvingModel(toGrpcRequest(model));
  }

  private static ModelRequest toGrpcRequest(String model) {
    return ModelRequest.newBuilder()
        .setModel(model)
        .build();
  }
}
