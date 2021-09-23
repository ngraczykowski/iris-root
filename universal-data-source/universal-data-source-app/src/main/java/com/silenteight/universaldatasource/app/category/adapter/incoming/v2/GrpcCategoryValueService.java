package com.silenteight.universaldatasource.app.category.adapter.incoming.v2;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.*;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@RequiredArgsConstructor
@GrpcService
class GrpcCategoryValueService extends CategoryValueServiceImplBase {

  private final CategoryAdapter categoryAdapter;

  @Override
  public void createCategoryValues(
      CreateCategoryValuesRequest request,
      StreamObserver<CreateCategoryValuesResponse> responseObserver) {

    responseObserver.onNext(categoryAdapter.createCategoryValues(request));
    responseObserver.onCompleted();
  }

  @Override
  public void batchCreateCategoryValues(
      BatchCreateCategoryValuesRequest request,
      StreamObserver<BatchCreateCategoryValuesResponse> responseObserver) {

    responseObserver.onNext(categoryAdapter.batchCreateCategoryValues(request));
    responseObserver.onCompleted();
  }

  @Override
  public void batchGetMatchesCategoryValues(
      BatchGetMatchesCategoryValuesRequest request,
      StreamObserver<BatchGetMatchesCategoryValuesResponse> responseObserver) {

    responseObserver.onNext(categoryAdapter.batchGetMatchCategoryValues(request));
    responseObserver.onCompleted();
  }
}
