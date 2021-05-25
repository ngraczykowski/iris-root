package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryValue;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
class FetchAllMissingCategoryValuesUseCase {

  private final MatchCategoryValuesDataAccess matchCategoryValuesDataAccess;
  private final DataSourceClient datasourceClient;

  void fetchAllMissingCategoryValues(String analysis) {
    var analysisId = ResourceName.create(analysis).getLong("analysis");

    log.info("Fetching missing category values: analysisId={}", analysisId);

    do {
      var missingValues =
          matchCategoryValuesDataAccess.getMissingCategoryValues(analysisId);

      if (missingValues.isEmpty()) {
        break;
      }

      if (log.isTraceEnabled()) {
        log.trace("Analysis is still missing match categories: analysisId={}, missingCount={}",
            analysisId, missingValues.getCount());
      }

      var response = requestMatchCategoryValues(missingValues.getMissingMatchCategories());

      storeMatchMissingCategoryValues(
          missingValues.getCategories(),
          response.getCategoryValuesList());
    } while (true);
  }


  private BatchGetMatchCategoryValuesResponse requestMatchCategoryValues(
      List<String> missingMatchCategories) {

    var request = BatchGetMatchCategoryValuesRequest.newBuilder()
        .addAllMatchValues(missingMatchCategories)
        .build();

    return datasourceClient.batchGetMatchCategoryValues(request);
  }

  private void storeMatchMissingCategoryValues(
      Map<String, Long> categories, List<CategoryValue> categoryValuesList) {

    matchCategoryValuesDataAccess.createMatchCategoryValues(
        categoryValuesList, categories);
  }
}
