package com.silenteight.universaldatasource.app.category.adapter.incoming.v1;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceImplBase;
import com.silenteight.datasource.categories.api.v1.ListCategoriesRequest;
import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Qualifier;

@GrpcService
class GrpcCategoryServiceV1 extends CategoryServiceImplBase {

  private final CategoryAdapter categoryAdapter;

  GrpcCategoryServiceV1(@Qualifier("categoryAdapterV1") CategoryAdapter categoryAdapter) {
    this.categoryAdapter = categoryAdapter;
  }

  @Override
  public void batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request,
      StreamObserver<BatchGetMatchCategoryValuesResponse> responseObserver) {

    responseObserver.onNext(categoryAdapter.batchGetMatchCategoryValues(request));
    responseObserver.onCompleted();
  }

  @Override
  public void listCategories(
      ListCategoriesRequest request,
      StreamObserver<ListCategoriesResponse> responseObserver) {

    responseObserver.onNext(categoryAdapter.listCategories());
    responseObserver.onCompleted();
  }
}
