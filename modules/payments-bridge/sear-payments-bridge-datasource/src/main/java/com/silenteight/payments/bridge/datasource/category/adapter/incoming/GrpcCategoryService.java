package com.silenteight.payments.bridge.datasource.category.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceImplBase;
import com.silenteight.datasource.categories.api.v1.ListCategoriesRequest;
import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcCategoryService extends CategoryServiceImplBase {

  private final CategoryAdapter categoryAdapter;

  @Override
  public void listCategories(
      ListCategoriesRequest request,
      StreamObserver<ListCategoriesResponse> responseObserver) {

    responseObserver.onNext(categoryAdapter.listCategories());
    responseObserver.onCompleted();
  }

  @Override
  public void batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request,
      StreamObserver<BatchGetMatchCategoryValuesResponse> responseObserver) {

    responseObserver.onNext(categoryAdapter.batchGetMatchCategoryValues(request));
    responseObserver.onCompleted();
  }
}
