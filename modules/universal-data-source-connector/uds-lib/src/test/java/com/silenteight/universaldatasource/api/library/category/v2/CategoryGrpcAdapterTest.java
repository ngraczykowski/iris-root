package com.silenteight.universaldatasource.api.library.category.v2;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesResponse;
import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc;
import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc.CategoryServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static com.silenteight.universaldatasource.api.library.category.v2.CategoryTypeShared.ENUMERATED;

class CategoryGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private CategoryGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedCategoryGrpcServer());

    var stub = CategoryServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new CategoryGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldCreateCategories() {
    //when
    var response = underTest.createCategories(Fixtures.REQUEST);

    Assertions.assertEquals(response.getCategories().size(), 1);

    CategoryShared first = response.getCategories().stream().findFirst().get();
    Assertions.assertEquals(first.getName(), Fixtures.NAME);
    Assertions.assertEquals(first.getDisplayName(), Fixtures.DISPLAY_NAME);
    Assertions.assertEquals(first.getAllowedValues(), Fixtures.ALLOWED_VALUE);
    Assertions.assertEquals(first.isMultiValue(), Fixtures.IS_MULTIVALUE);
  }

  static class MockedCategoryGrpcServer extends CategoryServiceImplBase {

    @Override
    public void batchCreateCategories(
        BatchCreateCategoriesRequest request,
        StreamObserver<BatchCreateCategoriesResponse> responseObserver) {
      responseObserver.onNext(Fixtures.GRPC_RESPONSE);
      responseObserver.onCompleted();
    }
  }

  static class Fixtures {

    public static final String NAME = "name";
    public static final String DISPLAY_NAME = "display name";
    public static final List<String> ALLOWED_VALUE = List.of("allowed-value");
    public static final boolean IS_MULTIVALUE = true;

    static final BatchCreateCategoriesIn REQUEST = BatchCreateCategoriesIn.builder()
        .categories(List.of(CategoryShared.builder()
            .name(NAME)
            .displayName(DISPLAY_NAME)
            .categoryType(ENUMERATED)
            .allowedValues(ALLOWED_VALUE)
            .multiValue(IS_MULTIVALUE)
            .build()))
        .build();

    static final BatchCreateCategoriesResponse GRPC_RESPONSE =
        BatchCreateCategoriesResponse.newBuilder()
            .addCategories(Category.newBuilder()
                .setName(NAME)
                .setDisplayName(DISPLAY_NAME)
                .setType(com.silenteight.datasource.categories.api.v2.CategoryType.ENUMERATED)
                .addAllAllowedValues(ALLOWED_VALUE)
                .setMultiValue(IS_MULTIVALUE)
                .build())
            .build();
  }
}
