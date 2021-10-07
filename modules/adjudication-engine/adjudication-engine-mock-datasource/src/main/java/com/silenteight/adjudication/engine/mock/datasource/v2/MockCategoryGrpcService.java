package com.silenteight.adjudication.engine.mock.datasource.v2;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@GrpcService
@Profile("mockdatasource & !datasourcev1")
@RequiredArgsConstructor
class MockCategoryGrpcService extends CategoryValueServiceImplBase {

  private final MockListCategoriesUseCase categoriesUseCase;
  private final MockGetMatchCategoryValuesUseCase categoryValuesUseCase;

  @Override
  public void batchGetMatchesCategoryValues(
      BatchGetMatchesCategoryValuesRequest request,
      StreamObserver<BatchGetMatchesCategoryValuesResponse> responseObserver) {
    var categoryValues = categoryValuesUseCase.getMatchCategoryValues(
        request.getCategoryMatchesList());
    responseObserver.onNext(
        BatchGetMatchesCategoryValuesResponse
            .newBuilder()
            .addAllCategoryValues(categoryValues)
            .build());
    responseObserver.onCompleted();
  }
}
