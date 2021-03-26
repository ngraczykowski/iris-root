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
  public SolvingModel getModel(String modelName) {
    return solvingModelStub.getSolvingModel(toGrpcRequest(modelName));
  }

  private static ModelRequest toGrpcRequest(String modelName) {
    return ModelRequest.newBuilder()
        .setModel(modelName)
        .build();
  }
}
