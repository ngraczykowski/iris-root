package com.silenteight.serp.governance.bulkchange.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.BulkBranchChangeGovernanceGrpc.BulkBranchChangeGovernanceImplBase;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesResponse;
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeRequest;
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeResponse;
import com.silenteight.serp.governance.bulkchange.BulkChangeQueries;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class BulkBranchChangeGrpcService extends BulkBranchChangeGovernanceImplBase {

  private final BulkChangeQueries bulkChangeQueries;

  @Override
  public void validateBulkChange(
      ValidateBulkChangeRequest request,
      StreamObserver<ValidateBulkChangeResponse> responseObserver) {

    responseObserver.onNext(bulkChangeQueries.validateBulkBranchChange(request));
    responseObserver.onCompleted();
  }

  @Override
  public void listBulkBranchChanges(
      ListBulkBranchChangesRequest request,
      StreamObserver<ListBulkBranchChangesResponse> responseObserver) {

    responseObserver.onNext(bulkChangeQueries.listBulkBranchChanges(request));
    responseObserver.onCompleted();
  }
}
