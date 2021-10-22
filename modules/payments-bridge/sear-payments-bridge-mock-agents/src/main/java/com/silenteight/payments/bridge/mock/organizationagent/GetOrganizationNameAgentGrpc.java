package com.silenteight.payments.bridge.mock.organizationagent;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.agent.organizationname.v1.api.CompareOrganizationNamesRequest;
import com.silenteight.proto.agent.organizationname.v1.api.CompareOrganizationNamesResponse;
import com.silenteight.proto.agent.organizationname.v1.api.OrganizationNameAgentGrpc.OrganizationNameAgentImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@Profile("mockagents")
@GrpcService
@RequiredArgsConstructor
class GetOrganizationNameAgentGrpc extends OrganizationNameAgentImplBase {

  @Override
  public void compareOrganizationNames(
      CompareOrganizationNamesRequest request,
      StreamObserver<CompareOrganizationNamesResponse> responseObserver) {
    responseObserver.onNext(
        CompareOrganizationNamesResponse.newBuilder()
            .setSolution("NO_MATCH")
            .build());
    responseObserver.onCompleted();
  }
}
