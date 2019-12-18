package com.silenteight.sens.webapp.backend.application.grpc.governance;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;

@RequiredArgsConstructor
class GrpcBranchGovernanceClient implements BranchGovernanceClient {

  private final BranchGovernanceBlockingStub branchGovernanceStub;
}
