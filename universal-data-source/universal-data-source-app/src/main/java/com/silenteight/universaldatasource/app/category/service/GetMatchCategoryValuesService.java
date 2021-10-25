package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryMatches;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.category.model.MatchCategoryRequest;
import com.silenteight.universaldatasource.app.category.port.incoming.GetMatchCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryValueDataAccess;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
class GetMatchCategoryValuesService implements GetMatchCategoryValuesUseCase {

  private final CategoryValueDataAccess categoryValueDataAccess;

  @Timed(value = "uds.category.use_cases", extraTags = { "action", "batchGetCategoryValues" })
  @Override
  public BatchGetMatchesCategoryValuesResponse batchGetMatchCategoryValues(
      List<CategoryMatches> matchValuesList) {

    if (log.isDebugEnabled()) {
      var matches = matchValuesList.stream()
          .flatMap(cm -> cm.getMatchesList().stream())
          .distinct()
          .collect(Collectors.toList());

      log.debug("Getting category values: categoryCount={}, matchCount={}, firstTenMatches={}",
          matchValuesList.size(), matches.size(), matches.subList(0, Math.min(10, matches.size())));
    }

    var matchCategoryList = getMatchCategoryList(matchValuesList);
    var categoryValues =
        categoryValueDataAccess.batchGetMatchCategoryValues(matchCategoryList);

    if (log.isDebugEnabled()) {
      log.debug("Returning category values: categoryValueCount={}", categoryValues.size());
    }

    return BatchGetMatchesCategoryValuesResponse.newBuilder()
        .addAllCategoryValues(categoryValues)
        .build();
  }

  private static List<MatchCategoryRequest> getMatchCategoryList(
      List<CategoryMatches> categoryMatches) {
    List<MatchCategoryRequest> matchCategoryRequestList = new ArrayList<>();

    for (CategoryMatches categoryMatch : categoryMatches) {
      var category = categoryMatch.getCategory();
      categoryMatch
          .getMatchesList()
          .stream()
          .map(match -> new MatchCategoryRequest(match, category))
          .forEach(matchCategoryRequestList::add);
    }
    return matchCategoryRequestList;
  }
}
