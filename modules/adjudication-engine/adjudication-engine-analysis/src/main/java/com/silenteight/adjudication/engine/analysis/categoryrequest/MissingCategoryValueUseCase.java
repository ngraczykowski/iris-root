package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;

import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
class MissingCategoryValueUseCase {

  private final MatchCategoryValuesDataAccess matchCategoryValuesDataAccess;
  private final DatasourceClient datasourceClient;

  void handleMissingCategoryValues(long analysisId) {
    boolean missingCategories = true;
    do {
      var missingCategoryValues =
          matchCategoryValuesDataAccess.getMissingCategoryValues(analysisId);
      var sizeOfMissing = missingCategoryValues.getMissingMatchCategories().size();
      missingCategories = sizeOfMissing > 0;
      if (missingCategories) {
        log.trace("Analysis {} is still missing match categories size:{}", analysisId,
            sizeOfMissing);
        collectAndStoreMatchCategories(missingCategoryValues);
      }
    } while (missingCategories);
  }


  private void collectAndStoreMatchCategories(MissingCategoryResult missingCategoryResult) {
    BatchGetMatchCategoryValuesResponse response =
        datasourceClient.batchGetMatchCategoryValues(BatchGetMatchCategoryValuesRequest.newBuilder()
            .addAllMatchValues(missingCategoryResult.getMissingMatchCategories())
            .build());
    storeMatchMissingCategoryValues(response, missingCategoryResult.getCategories());
  }

  private void storeMatchMissingCategoryValues(
      BatchGetMatchCategoryValuesResponse response,
      Map<String, Long> categories) {
    matchCategoryValuesDataAccess.createMatchCategoryValues(
        response.getCategoryValuesList(),
        categories);
  }

}
