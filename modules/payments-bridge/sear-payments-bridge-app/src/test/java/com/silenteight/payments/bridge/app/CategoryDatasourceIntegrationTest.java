package com.silenteight.payments.bridge.app;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceBlockingStub;
import com.silenteight.payments.bridge.datasource.category.model.MatchCategoryValue;
import com.silenteight.payments.bridge.datasource.category.port.incoming.AddCategoryValuesUseCase;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@SpringBootTest(
    classes = PaymentsBridgeApplication.class,
    properties = "debug=true")
@ActiveProfiles({ "test" })
@EnableConfigurationProperties
class CategoryDatasourceIntegrationTest {

  @GrpcClient("pb")
  private CategoryServiceBlockingStub categoryServiceBlockingStub;

  @Autowired
  private AddCategoryValuesUseCase addCategoryValuesUseCase;

  @BeforeEach
  void setUp() {
    addCategoryValuesUseCase.addCategoryValues(getCategoryValues());
  }

  @Test
  void testGettingCategoryValues() {

    await()
        .atLeast(Duration.ofSeconds(3L))
        .atMost(Duration.ofSeconds(10L));

    BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValuesResponse =
        categoryServiceBlockingStub.batchGetMatchCategoryValues(
            BatchGetMatchCategoryValuesRequest.newBuilder()
                .addAllMatchValues(List.of(
                    "matches/8/categories/SpecificTerms",
                    "matches/8/categories/NameAddressCrossMatch"))
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
    assertThat(batchGetMatchCategoryValuesResponse.getCategoryValuesList()).isNotEqualTo(null);
    assertThat(firstCategorySingleValue).isEqualTo("YES");
    assertThat(secondCategorySingleValue).isEqualTo("NO");
    assertThat(firstCategoryName).isEqualTo("categoriesSpecificTerms/matches/8");
    assertThat(secondCategoryName).isEqualTo("categoriesNameAddressCrossMatch/matches/8");
  }

  private static List<MatchCategoryValue> getCategoryValues() {
    MatchCategoryValue matchCategoryValueOne = MatchCategoryValue.builder()
        .match("8")
        .category("SpecificTerms")
        .value("YES")
        .build();
    MatchCategoryValue matchCategoryValueTwo = MatchCategoryValue.builder()
        .match("8")
        .category("NameAddressCrossMatch")
        .value("NO")
        .build();
    MatchCategoryValue matchCategoryValueThree = MatchCategoryValue.builder()
        .match("8")
        .category("Strip")
        .value("YES")
        .build();

    return List.of(matchCategoryValueOne, matchCategoryValueTwo, matchCategoryValueThree);
  }

}
