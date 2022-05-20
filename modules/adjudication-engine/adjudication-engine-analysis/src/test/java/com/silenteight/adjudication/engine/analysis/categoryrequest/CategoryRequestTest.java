package com.silenteight.adjudication.engine.analysis.categoryrequest;

import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.CategoryMap;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MatchAlert;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingCategoryResult;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingMatchCategory;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;
import com.silenteight.adjudication.internal.v1.PendingRecommendations.Builder;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class CategoryRequestTest {

  private HandlePendingRecommendationsUseCase handlePendingRecommendationsUseCase;
  private MatchCategoryValuesDataAccess matchCategoryValuesDataAccess;
  private CategoryServiceClient datasourceClient;

  @BeforeEach
  void setUp() {
    matchCategoryValuesDataAccess = Mockito.mock(MatchCategoryValuesDataAccess.class);
    datasourceClient = Mockito.mock(CategoryServiceClient.class);
    handlePendingRecommendationsUseCase = new HandlePendingRecommendationsUseCase(
        new FetchAllMissingCategoryValuesUseCase(matchCategoryValuesDataAccess, datasourceClient));
  }

  @Test
  void shouldHandleRequest() {
    var categoryMap = new CategoryMap(Map.of("categories/country", 1L));

    when(
        datasourceClient.batchGetMatchCategoryValues(
            any(BatchGetMatchesCategoryValuesRequest.class)))
        .thenReturn(BatchGetMatchesCategoryValuesResponse.newBuilder().build());
    when(matchCategoryValuesDataAccess.getMissingCategoryValues(1))
        .thenReturn(
            new MissingCategoryResult(
                List.of(MissingMatchCategory
                    .builder()
                    .categoryName("categories/country")
                    .matches(List.of(
                        MatchAlert.builder().alertId(1L).matchId(1L).build()))
                    .build()), categoryMap)
        )
        .thenReturn(new MissingCategoryResult(List.of(), new CategoryMap()));

    assertDoesNotThrow(() ->
        handlePendingRecommendationsUseCase.handlePendingRecommendations(dataset().build())
    );
  }

  private static Builder dataset() {
    return PendingRecommendations.newBuilder().addAnalysis("analysis/1");
  }
}
