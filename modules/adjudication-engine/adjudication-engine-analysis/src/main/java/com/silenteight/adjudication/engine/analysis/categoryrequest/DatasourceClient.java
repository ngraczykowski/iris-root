package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class DatasourceClient {

  @GrpcClient("datasource")
  private CategoryServiceBlockingStub categoryServiceStub;

  public BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request) {
    return categoryServiceStub.batchGetMatchCategoryValues(request);
  }

}
