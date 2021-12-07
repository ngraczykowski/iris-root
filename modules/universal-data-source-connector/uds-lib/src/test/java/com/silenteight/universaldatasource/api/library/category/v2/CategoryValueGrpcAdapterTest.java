package com.silenteight.universaldatasource.api.library.category.v2;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceImplBase;
import com.silenteight.datasource.categories.api.v2.CreatedCategoryValue;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class CategoryValueGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private CategoryValuesGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedCategoryGrpcServer());

    var stub = CategoryValueServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new CategoryValuesGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldCreateCategoriesValues() {
    //when
    var response = underTest.createCategoriesValues(Fixtures.REQUEST);

    Assertions.assertEquals(response.getCreatedCategoryValues().size(), 1);

    var first = response.getCreatedCategoryValues().stream().findFirst().get();
    Assertions.assertEquals(first.getName(), Fixtures.CATEGORY_VALUE_NAME);
    Assertions.assertEquals(first.getMatch(), Fixtures.CATEGORY_VALUE_MATCH);
  }

  static class MockedCategoryGrpcServer extends CategoryValueServiceImplBase {

    @Override
    public void batchCreateCategoryValues(
        BatchCreateCategoryValuesRequest request,
        StreamObserver<BatchCreateCategoryValuesResponse> responseObserver) {
      responseObserver.onNext(Fixtures.GRPC_RESPONSE);
      responseObserver.onCompleted();
    }
  }

  static class Fixtures {

    public static final String CATEGORY = "category";
    public static final String CATEGORY_VALUE_NAME = "category value name";
    public static final String CATEGORY_VALUE_MATCH = "category value match";

    static final MultiValueIn MULTI_VALUE = MultiValueIn.builder()
        .values(List.of("one", "two"))
        .build();

    static final List<CategoryValueIn> CATEGORY_VALUE_INS = List.of(
        CategoryValueIn.builder()
            .name(CATEGORY_VALUE_NAME)
            .match(CATEGORY_VALUE_MATCH)
            .multiValue(MULTI_VALUE)
            .build()
    );

    static final List<CreateCategoryValuesIn> CREATE_CATEGORY_VALUES_IN = List.of(
        CreateCategoryValuesIn.builder()
            .category(CATEGORY)
            .categoryValues(CATEGORY_VALUE_INS)
            .build()
    );

    static final BatchCreateCategoryValuesIn REQUEST = BatchCreateCategoryValuesIn.builder()
        .requests(CREATE_CATEGORY_VALUES_IN)
        .build();

    static final CreatedCategoryValue CREATED_CATEGORY_VALUE = CreatedCategoryValue.newBuilder()
        .setName(CATEGORY_VALUE_NAME)
        .setMatch(CATEGORY_VALUE_MATCH)
        .build();

    static final BatchCreateCategoryValuesResponse GRPC_RESPONSE =
        BatchCreateCategoryValuesResponse.newBuilder()
            .addCreatedCategoryValues(CREATED_CATEGORY_VALUE)
            .build();
  }
}
