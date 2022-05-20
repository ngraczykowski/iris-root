package com.silenteight.adjudication.engine.analysis.categoryrequest;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceImplBase;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;
import com.silenteight.sep.base.testing.grpc.GrpcServerExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class CategoryServiceClientTest {

  private CategoryServiceClient categoryServiceClient;

  @RegisterExtension
  GrpcServerExtension grpcServer = new GrpcServerExtension().directExecutor();

  @BeforeEach
  void setUp() {
    MockedCategoryServiceImplBase service = spy(new MockedCategoryServiceImplBase());
    grpcServer.addService(service);
    CategoryValueServiceBlockingStub stub = CategoryValueServiceGrpc
        .newBlockingStub(grpcServer.getChannel());
    Duration timeout = Duration.ofMillis(500L);
    categoryServiceClient = new CategoryServiceClient(stub, timeout);
  }

  @Test
  void testBatchGetMatchCategoryValues() {

    BatchGetMatchesCategoryValuesRequest request =
        BatchGetMatchesCategoryValuesRequest.getDefaultInstance();
    assertDoesNotThrow(() ->
        categoryServiceClient.batchGetMatchCategoryValues(request)
    );
  }

  class MockedCategoryServiceImplBase extends CategoryServiceImplBase {

    @Override
    public void batchGetMatchCategoryValues(
        BatchGetMatchCategoryValuesRequest request,
        io.grpc.stub.StreamObserver<BatchGetMatchCategoryValuesResponse> responseObserver) {
      try {
        Thread.sleep(10000L);
        Assertions.fail();
      } catch (InterruptedException e) {
        throw new AssertionError();
      }
    }
  }
}
