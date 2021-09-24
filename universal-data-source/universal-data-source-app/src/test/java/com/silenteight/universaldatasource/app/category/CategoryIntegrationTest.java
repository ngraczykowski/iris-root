package com.silenteight.universaldatasource.app.category;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc;
import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc.CategoryServiceBlockingStub;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.ListCategoriesRequest;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.universaldatasource.app.UniversalDataSourceApplication;

import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.List;

import static com.silenteight.universaldatasource.app.category.CategoryIntegrationTestFixture.getBatchCategories;
import static com.silenteight.universaldatasource.app.category.CategoryIntegrationTestFixture.getBatchCategoryValueRequest;
import static com.silenteight.universaldatasource.app.category.CategoryIntegrationTestFixture.getBatchCreateCategoryValuesRequest;
import static com.silenteight.universaldatasource.app.category.CategoryIntegrationTestFixture.getCreateCategoryValuesRequest;
import static com.silenteight.universaldatasource.app.category.CategoryTestDataAccess.streamedCategoriesCount;
import static com.silenteight.universaldatasource.app.category.CategoryTestDataAccess.streamedCategoryValueCount;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@SpringBootTest(
    classes = UniversalDataSourceApplication.class,
    properties = "debug=true")
@ActiveProfiles({ "test" })
@EnableConfigurationProperties
class CategoryIntegrationTest {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @GrpcClient("uds")
  private CategoryServiceGrpc.CategoryServiceBlockingStub categoryServiceBlockingStubV1;

  @GrpcClient("uds")
  private CategoryServiceBlockingStub categoryServiceBlockingStub;

  @GrpcClient("uds")
  private CategoryValueServiceBlockingStub categoryValueServiceBlockingStub;

  @BeforeEach
  void setUp() {
    categoryServiceBlockingStub.batchCreateCategories(getBatchCategories());
  }

  @Test
  void testGettingCategories() {
    await()
        .atMost(Duration.ofSeconds(5))
        .until(() -> streamedCategoriesCount(jdbcTemplate
        ) > 0);

    var listCategoriesResponse =
        categoryServiceBlockingStub.listCategories(ListCategoriesRequest.getDefaultInstance());

    assertThat(listCategoriesResponse.getCategoriesCount()).isEqualTo(3);
    assertThat((int) listCategoriesResponse
        .getCategoriesList()
        .stream()
        .filter(c -> c.getName().equals("categories/categoryOne")).count()).isEqualTo(1);
  }

  @Test
  void testCreateCategoryValues() {

    var categoryValues =
        categoryValueServiceBlockingStub.createCategoryValues(
            getCreateCategoryValuesRequest("categoryOne"));

    assertThat(categoryValues.getCreatedCategoryValuesCount()).isEqualTo(3);
    assertThat((int) categoryValues
        .getCreatedCategoryValuesList()
        .stream()
        .filter(c -> c.getMatch().equals("alerts/alertOne/matches/matchOne")).count()).isEqualTo(1);
  }

  @Test
  void testBatchCreateCategoryValues() {

    addBatchCategoryValues("categoryThree");
    addBatchCategoryValues("categoryThree");

    var batchGetMatchesCategoryValuesResponse =
        categoryValueServiceBlockingStub.batchGetMatchesCategoryValues(
            getBatchCategoryValueRequest());

    assertThat(batchGetMatchesCategoryValuesResponse.getCategoryValuesCount()).isEqualTo(4);
    assertThat((int) batchGetMatchesCategoryValuesResponse
        .getCategoryValuesList()
        .stream()
        .filter(c -> c.getMatch().equals("alerts/alertOne/matches/matchOne")).count()).isEqualTo(2);
  }

  private void addBatchCategoryValues(String category) {
    var categoryValues =
        categoryValueServiceBlockingStub.batchCreateCategoryValues(
            getBatchCreateCategoryValuesRequest(category));

    await()
        .atMost(Duration.ofSeconds(5))
        .until(() -> streamedCategoryValueCount(jdbcTemplate
        ) > 0);

    assertThat(categoryValues.getCreatedCategoryValuesCount()).isEqualTo(3);
  }

  @Test
  void testGettingCategoryValuesVersionOne() {

    addBatchCategoryValues("categoryOne");
    addBatchCategoryValues("categoryTwo");

    BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValuesResponse =
        categoryServiceBlockingStubV1.batchGetMatchCategoryValues(
            BatchGetMatchCategoryValuesRequest.newBuilder()
                .addAllMatchValues(List.of(
                    "categories/categoryOne/alerts/alertOne/matches/matchOne",
                    "categories/categoryTwo/alerts/alertTwo/matches/matchTwo"))
                .build()
        );

    var firstCategorySingleValue =
        batchGetMatchCategoryValuesResponse.getCategoryValuesList().get(0).getSingleValue();
    var firstCategoryName =
        batchGetMatchCategoryValuesResponse.getCategoryValuesList().get(0).getName();
    var secondCategorySingleValue =
        batchGetMatchCategoryValuesResponse.getCategoryValuesList().get(1).getSingleValue();
    var secondCategoryName =
        batchGetMatchCategoryValuesResponse.getCategoryValuesList().get(1).getName();

    assertThat(batchGetMatchCategoryValuesResponse.getCategoryValuesCount()).isEqualTo(2);
    assertThat(batchGetMatchCategoryValuesResponse.getCategoryValuesList()).isNotNull();
    assertThat(firstCategorySingleValue).isEqualTo("NO");
    assertThat(secondCategorySingleValue).isEqualTo("YES");
    assertThat(firstCategoryName).contains("categories/categoryTwo/value/");
    assertThat(secondCategoryName).contains("categories/categoryOne/value/");
  }

  @Test
  void testBatchCreateCategoryValuesToNonExistingCategories() {
    assertThrows(
        StatusRuntimeException.class,
        () -> addBatchCategoryValues("Nonexisting category")
    );
  }

  @Test
  void testBatchCreateCategoryValuesWithNonExistingCategoryValue() {

    assertThrows(
        StatusRuntimeException.class,
        () -> categoryValueServiceBlockingStub.batchCreateCategoryValues(
            BatchCreateCategoryValuesRequest.newBuilder()
                .addRequests(CreateCategoryValuesRequest.newBuilder()
                    .setCategory("categories/categoryThree")
                    .addAllCategoryValues(
                        List.of(
                            CategoryValue.newBuilder()
                                .setMatch("alerts/alertOne/matches/matchOne")
                                .setSingleValue("NonExistingValue")
                                .build()
                        )
                    )
                    .build())
                .build()
        )
    );
  }
}

