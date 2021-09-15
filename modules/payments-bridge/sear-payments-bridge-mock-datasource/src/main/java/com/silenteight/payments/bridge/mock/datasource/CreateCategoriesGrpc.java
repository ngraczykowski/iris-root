package com.silenteight.payments.bridge.mock.datasource;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@Profile("mockdatasource")
@GrpcService
@RequiredArgsConstructor
class CreateCategoriesGrpc extends CategoryServiceGrpc.CategoryServiceImplBase {

  @Override
  public void batchCreateCategories(
      BatchCreateCategoriesRequest request,
      StreamObserver<BatchCreateCategoriesResponse> responseObserver) {
    responseObserver.onNext(BatchCreateCategoriesResponse.newBuilder().build());
    responseObserver.onCompleted();
  }
}
