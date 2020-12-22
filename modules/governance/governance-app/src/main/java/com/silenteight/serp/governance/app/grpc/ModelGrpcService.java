package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.GetPipelineModelRequest;
import com.silenteight.proto.serp.v1.api.ModelGovernanceGrpc;
import com.silenteight.proto.serp.v1.api.PipelineModel;
import com.silenteight.proto.serp.v1.model.Model;
import com.silenteight.serp.governance.model.ModelFinder;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class ModelGrpcService extends ModelGovernanceGrpc.ModelGovernanceImplBase {

  private final ModelFinder models;

  @Override
  public void getPipelineModel(
      GetPipelineModelRequest request,
      StreamObserver<PipelineModel> responseObserver) {

    ByteString modelSignature = request.getPipelineModelSignature();

    Model model = models.getBySignature(modelSignature);

    responseObserver.onNext(toPipelineModel(model));
    responseObserver.onCompleted();
  }

  private static PipelineModel toPipelineModel(Model model) {
    return PipelineModel
        .newBuilder()
        .setModel(model)
        .build();
  }
}
