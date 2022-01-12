package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.datasource.api.name.v1.NameInputServiceGrpc.NameInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.concurrent.atomic.AtomicLong;

@GrpcService
@RequiredArgsConstructor
@Slf4j
class GrpcNameInputService extends NameInputServiceImplBase {

  private final FeatureAdapter featureAdapter;
  private AtomicLong idx = new AtomicLong(0);

  @Override
  public void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request,
      StreamObserver<BatchGetMatchNameInputsResponse> responseObserver) {
    long requestId = idx.getAndIncrement();
    featureAdapter.batchGetMatchNameInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
