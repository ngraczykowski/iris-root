package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceImplBase;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcAgentInputService extends AgentInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchCreateAgentInputs(
      BatchCreateAgentInputsRequest request,
      StreamObserver<BatchCreateAgentInputsResponse> responseObserver) {

    responseObserver.onNext(featureAdapter.batchAgentInputs(request));
    responseObserver.onCompleted();
  }
}
