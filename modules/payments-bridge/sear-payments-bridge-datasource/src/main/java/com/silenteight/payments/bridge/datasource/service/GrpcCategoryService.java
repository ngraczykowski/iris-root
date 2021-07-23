package com.silenteight.payments.bridge.datasource.service;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceImplBase;
import com.silenteight.datasource.categories.api.v1.ListCategoriesRequest;
import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
class GrpcCategoryService extends CategoryServiceImplBase {

  @Override
  public void listCategories(
      ListCategoriesRequest request,
      StreamObserver<ListCategoriesResponse> responseObserver) {

    // TODO(ahaczewski): Implement listCategories.
    super.listCategories(request, responseObserver);
  }

  @Override
  public void batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request,
      StreamObserver<BatchGetMatchCategoryValuesResponse> responseObserver) {

    // TODO(ahaczewski): Implement batchGetMatchCategoryValues.
    responseObserver.onError(Status.UNIMPLEMENTED.asRuntimeException());
  }
}
