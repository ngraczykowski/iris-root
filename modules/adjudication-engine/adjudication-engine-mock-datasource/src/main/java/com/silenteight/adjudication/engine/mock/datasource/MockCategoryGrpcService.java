package com.silenteight.adjudication.engine.mock.datasource;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceImplBase;
import com.silenteight.datasource.categories.api.v1.ListCategoriesRequest;
import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@GrpcService
@Profile("mock-datasource")
@RequiredArgsConstructor
class MockCategoryGrpcService extends CategoryServiceImplBase {

  private final MockListCategoriesUseCase categoriesUseCase;
  private final MockGetMatchCategoryValuesUseCase categoryValuesUseCase;

  @Override
  public void listCategories(
      ListCategoriesRequest request,
      StreamObserver<ListCategoriesResponse> responseObserver) {
    var categories = categoriesUseCase.findAllCategories();
    responseObserver.onNext(
        ListCategoriesResponse.newBuilder().addAllCategories(categories).build());
    responseObserver.onCompleted();
  }

  @Override
  public void batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request,
      StreamObserver<BatchGetMatchCategoryValuesResponse> responseObserver) {
    var categoryValues = categoryValuesUseCase.getMatchCategoryValues(
        request.getMatchValuesList());
    responseObserver.onNext(
        BatchGetMatchCategoryValuesResponse
            .newBuilder()
            .addAllCategoryValues(categoryValues)
            .build());
    responseObserver.onCompleted();
  }
}
