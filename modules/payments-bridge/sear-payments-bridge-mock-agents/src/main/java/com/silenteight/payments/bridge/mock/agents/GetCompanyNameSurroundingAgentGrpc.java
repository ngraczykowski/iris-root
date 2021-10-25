package com.silenteight.payments.bridge.mock.agents;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingRequest;
import com.silenteight.proto.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingResponse;
import com.silenteight.proto.agent.companynamesurrounding.v1.api.CompanyNameSurroundingAgentGrpc.CompanyNameSurroundingAgentImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@Profile("mockagents")
@GrpcService
@RequiredArgsConstructor
class GetCompanyNameSurroundingAgentGrpc extends CompanyNameSurroundingAgentImplBase {


  @Override
  public void checkCompanyNameSurrounding(
      CheckCompanyNameSurroundingRequest request,
      StreamObserver<CheckCompanyNameSurroundingResponse> responseObserver) {
    responseObserver.onNext(
        CheckCompanyNameSurroundingResponse.newBuilder()
            .addAllNames(request.getNamesList())
            .setResult(1)
            .setSolution("MATCH_1")
            .build());
    responseObserver.onCompleted();
  }
}
