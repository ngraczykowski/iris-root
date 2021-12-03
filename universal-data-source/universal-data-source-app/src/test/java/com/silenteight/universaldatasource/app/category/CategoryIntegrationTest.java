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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.silenteight.universaldatasource.app.category.CategoryIntegrationTestFixture.getBatchCategoryValueRequest;
import static com.silenteight.universaldatasource.app.category.CategoryIntegrationTestFixture.getBatchCreateCategoryValuesRequest;
import static com.silenteight.universaldatasource.app.category.CategoryIntegrationTestFixture.getCreateCategoryValuesRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

  @Test
  @Sql(scripts = "adapter/outgoing/jdbc/populate_categories.sql")
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_categories.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void testGettingCategories() {

    var listCategoriesResponse =
        categoryServiceBlockingStub.listCategories(ListCategoriesRequest.getDefaultInstance());

    assertThat(listCategoriesResponse.getCategoriesCount()).isEqualTo(4);
    assertThat((int) listCategoriesResponse
        .getCategoriesList()
        .stream()
        .filter(c -> c.getName().equals("categories/categoryOne")).count()).isEqualTo(1);
  }

  @Test
  @Sql(scripts = "adapter/outgoing/jdbc/populate_categories.sql")
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_categories.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void testCreateCategoryValues() {

    var categoryValues =
        categoryValueServiceBlockingStub.createCategoryValues(
            getCreateCategoryValuesRequest("categoryFour"));

    assertThat(categoryValues.getCreatedCategoryValuesCount()).isEqualTo(3);
    assertThat((int) categoryValues
        .getCreatedCategoryValuesList()
        .stream()
        .filter(c -> c.getMatch().equals("alerts/1/matches/1")).count()).isEqualTo(1);
  }

  @Test
  @Sql(scripts = { "adapter/outgoing/jdbc/populate_categories.sql", })
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_categories.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void testBatchCreateCategoryValues() {

    var batchGetMatchesCategoryValuesResponse =
        categoryValueServiceBlockingStub.batchGetMatchesCategoryValues(
            getBatchCategoryValueRequest());

    assertThat(batchGetMatchesCategoryValuesResponse.getCategoryValuesCount()).isEqualTo(2);
    assertThat((int) batchGetMatchesCategoryValuesResponse
        .getCategoryValuesList()
        .stream()
        .filter(c -> c.getMatch().equals("alerts/1/matches/1")).count()).isEqualTo(1);
  }

  @Test
  @Sql(scripts = "adapter/outgoing/jdbc/populate_categories.sql")
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_categories.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void testGettingCategoryValuesVersionOne() {

    BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValuesResponse =
        categoryServiceBlockingStubV1.batchGetMatchCategoryValues(
            BatchGetMatchCategoryValuesRequest.newBuilder()
                .addAllMatchValues(List.of(
                    "categories/categoryOne/alerts/1/matches/1",
                    "categories/categoryTwo/alerts/2/matches/2"))
                .build()
        );

    assertThat(batchGetMatchCategoryValuesResponse.getCategoryValuesCount()).isEqualTo(2);

    assertThat(batchGetMatchCategoryValuesResponse.getCategoryValuesList().stream()
        .filter(c -> c.getSingleValue().equals("NO"))
        .filter(c -> c.getName().startsWith("categories/categoryTwo/values/"))
        .count()).isEqualTo(1);

    assertThat(batchGetMatchCategoryValuesResponse.getCategoryValuesList().stream()
        .filter(c -> c.getSingleValue().equals("YES"))
        .filter(c -> c.getName().startsWith("categories/categoryOne/values/"))
        .count()).isEqualTo(1);
  }

  @Test
  void testBatchCreateCategoryValuesToNonExistingCategories() {
    assertThrows(
        StatusRuntimeException.class,
        this::addInvalidCategoryValue
    );
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void addInvalidCategoryValue() {
    categoryValueServiceBlockingStub.batchCreateCategoryValues(
        getBatchCreateCategoryValuesRequest("Nonexistent category"));
  }

  @Test
  @Sql(scripts = "adapter/outgoing/jdbc/populate_categories.sql")
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_categories.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @SuppressWarnings("ResultOfMethodCallIgnored")
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
                                .setMatch("alerts/1/matches/1")
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
