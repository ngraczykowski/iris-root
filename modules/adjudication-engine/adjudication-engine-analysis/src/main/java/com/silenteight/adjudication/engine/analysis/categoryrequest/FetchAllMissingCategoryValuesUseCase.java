package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.MatchCategoriesUpdated;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.HashSet;

@RequiredArgsConstructor
@Service
@Slf4j
class FetchAllMissingCategoryValuesUseCase {

  private final MatchCategoryValuesDataAccess matchCategoryValuesDataAccess;
  private final CategoryServiceClient categoryServiceClient;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "categoryrequest" })
  MatchCategoriesUpdated fetchAllMissingCategoryValues(String analysis) {
    log.info("Fetching missing category values: analysis={}", analysis);

    var analysisId = ResourceName.create(analysis).getLong("analysis");
    var categories = new HashSet<String>();
    var matches = new HashSet<String>();
    do {
      var missingValues =
          matchCategoryValuesDataAccess.getMissingCategoryValues(analysisId);

      log.info("Received missing values = {}", missingValues);

      if (missingValues.isEmpty()) {
        log.debug("No more missing category values: analysis={}, categoryCount={}, matchCount={}",
            analysis, categories.size(), matches.size());
        break;
      }

      if (log.isTraceEnabled()) {
        log.trace("Analysis is missing match category values: analysis={}, missingCount={}",
            analysis, missingValues.getCount());
      }

      var response =
          categoryServiceClient.getCategoryValue(missingValues);

      matchCategoryValuesDataAccess.createMatchCategoryValues(
          missingValues.getCategoryMap(), response);

      categories.addAll(missingValues.getCategories());
      missingValues.forEachMatch(matches::add);
    } while (true);

    log.info("Fetched all missing category values: analysis={}, categories={}, matches={}",
        analysis, categories, matches);

    return MatchCategoriesUpdated
        .newBuilder()
        .setAnalysis(analysis)
        .addAllCategories(categories)
        .addAllMatches(matches)
        .build();
  }
}
