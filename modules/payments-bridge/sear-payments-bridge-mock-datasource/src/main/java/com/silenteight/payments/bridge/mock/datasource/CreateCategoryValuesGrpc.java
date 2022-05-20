package com.silenteight.payments.bridge.mock.datasource;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@Profile("mockdatasource")
@GrpcService
@RequiredArgsConstructor
class CreateCategoryValuesGrpc extends CategoryValueServiceGrpc.CategoryValueServiceImplBase {

  private final MockDatasourceService mockDatasourceService;

  @Override
  public void batchCreateCategoryValues(
      BatchCreateCategoryValuesRequest request,
      StreamObserver<BatchCreateCategoryValuesResponse> responseObserver) {
    responseObserver.onNext(mockDatasourceService.createCategoryValuesResponse(request));
    responseObserver.onCompleted();
  }
}
