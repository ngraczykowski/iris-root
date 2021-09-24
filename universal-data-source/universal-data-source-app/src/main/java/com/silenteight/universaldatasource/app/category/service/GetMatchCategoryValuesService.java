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

@Slf4j
@RequiredArgsConstructor
@Service
class GetMatchCategoryValuesService implements GetMatchCategoryValuesUseCase {

  private final CategoryValueDataAccess categoryValueDataAccess;

  @Timed(value = "uds.category.use_cases", extraTags = { "action", "batchGetCategoryValues" })
  @Override
  public BatchGetMatchesCategoryValuesResponse batchGetMatchCategoryValues(
      List<CategoryMatches> matchValuesList) {

    log.debug("Getting category values : categoryValuesCount={}", matchValuesList.size());

    var matchCategoryList = getMatchCategoryList(matchValuesList);
    var categoryValues =
        categoryValueDataAccess.batchGetMatchCategoryValues(matchCategoryList);

    return BatchGetMatchesCategoryValuesResponse.newBuilder()
        .addAllCategoryValues(categoryValues)
        .build();
  }

  private List<MatchCategoryRequest> getMatchCategoryList(List<CategoryMatches> categoryMatches) {
    List<MatchCategoryRequest> matchCategoryRequestList = new ArrayList<>();

    for (CategoryMatches categoryMatch : categoryMatches) {
      var category = categoryMatch.getCategories();
      categoryMatch
          .getMatchesList()
          .stream()
          .map(match -> new MatchCategoryRequest(match, category))
          .forEach(matchCategoryRequestList::add);
    }
    return matchCategoryRequestList;
  }
}
