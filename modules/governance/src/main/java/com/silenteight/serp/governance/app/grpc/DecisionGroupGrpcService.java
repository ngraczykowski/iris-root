package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.DecisionGroupGovernanceGrpc;

import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class DecisionGroupGrpcService extends DecisionGroupGovernanceGrpc.DecisionGroupGovernanceImplBase {
}
