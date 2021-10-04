package com.silenteight.universaldatasource.app.category.adapter.incoming.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc.CategoryServiceImplBase;
import com.silenteight.datasource.categories.api.v2.ListCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.ListCategoriesResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@RequiredArgsConstructor
@GrpcService
class GrpcCategoryService extends CategoryServiceImplBase {

  private final CategoryAdapter categoryAdapter;

  @Override
  public void batchCreateCategories(
      BatchCreateCategoriesRequest request,
      StreamObserver<BatchCreateCategoriesResponse> responseObserver) {

    log.info("Batch create categories request received");

    responseObserver.onNext(categoryAdapter.batchCreateCategories(request));
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
