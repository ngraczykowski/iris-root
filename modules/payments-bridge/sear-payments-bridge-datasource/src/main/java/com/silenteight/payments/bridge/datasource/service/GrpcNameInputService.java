package com.silenteight.payments.bridge.datasource.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.datasource.api.name.v1.NameInputServiceGrpc.NameInputServiceImplBase;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcNameInputService extends NameInputServiceImplBase {

  private final NameInputService nameInputService;

  @Override
  public void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request,
      StreamObserver<BatchGetMatchNameInputsResponse> responseObserver) {

    // The implementation shall look more or less like this:
    //
    // foreach (name input requested)
    //   responseObserver.onNext();
    //
    // responseObserver.onCompleted();
    //
    // WARNING: The implementation MUST be "reactive", i.e., the implementation MUST NOT
    //  fetch all the requested data from the DB, and then stream it, instead it MUST
    //  respond with the data from the DB as they are delivered.
    //
    // Example of how to achieve the above is in Adjudication Engine source code, in
    // `GrpcRecommendationService#streamRecommendationsWithMetadata()` implementation.

    // TODO(ahaczewski): Implement batchGetMatchNameInputs.
    responseObserver.onError(Status.UNIMPLEMENTED.asRuntimeException());
  }
}
