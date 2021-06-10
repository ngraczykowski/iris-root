package com.silenteight.adjudication.engine.analysis.categoryrequest;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;
import com.silenteight.adjudication.internal.v1.PendingRecommendations.Builder;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;

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
  private DataSourceClient datasourceClient;

  @BeforeEach
  void setUp() {
    matchCategoryValuesDataAccess = Mockito.mock(MatchCategoryValuesDataAccess.class);
    datasourceClient = Mockito.mock(DataSourceClient.class);
    handlePendingRecommendationsUseCase = new HandlePendingRecommendationsUseCase(
        new FetchAllMissingCategoryValuesUseCase(matchCategoryValuesDataAccess, datasourceClient));
  }

  @Test
  void shouldHandleRequest() {
    var categoryMap = new CategoryMap(Map.of("categories/country", 1L));

    when(
        datasourceClient.batchGetMatchCategoryValues(any(BatchGetMatchCategoryValuesRequest.class)))
        .thenReturn(BatchGetMatchCategoryValuesResponse.newBuilder().build());
    when(matchCategoryValuesDataAccess.getMissingCategoryValues(1))
        .thenReturn(
            new MissingCategoryResult(
                List.of("categories/country/matches/1"), categoryMap)
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
