package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryMatches;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.category.model.MatchCategoryRequest;
import com.silenteight.universaldatasource.app.category.port.incoming.GetMatchCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryValueDataAccess;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
class GetMatchCategoryValuesService implements GetMatchCategoryValuesUseCase {

  private static final String NO_DATA_VALUE = "NO_DATA";

  private final CategoryValueDataAccess categoryValueDataAccess;

  @Timed(
      value = "uds.category.use_cases",
      extraTags = { "action", "batchGetCategoryValues" },
      histogram = true,
      percentiles = { 0.5, 0.95, 0.99 }
  )
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

    var mockedCategoryValues = mockUnseenMatchOutputs(matchCategoryList, categoryValues);
    categoryValues.addAll(mockedCategoryValues);

    if (log.isDebugEnabled()) {
      log.debug("Returning category values: categoryValueCount={}", categoryValues.size());
    }

    return BatchGetMatchesCategoryValuesResponse.newBuilder()
        .addAllCategoryValues(categoryValues)
        .build();
  }

  private Collection<CategoryValue> mockUnseenMatchOutputs(
      List<MatchCategoryRequest> matchCategoryList, Collection<CategoryValue> categoryValues) {
    var mockedCategoryValues = matchCategoryList.stream()
        .filter(mc -> !categoryValuesContains(categoryValues, mc))
        .map(this::createCategoryValue)
        .collect(Collectors.toList());

    if (log.isDebugEnabled()) {

      var categoriesCount = mockedCategoryValues.stream()
          .collect(Collectors.groupingBy(CategoryValue::getName, Collectors.counting()));

      log.debug(
          "Sending mocked unseen matches - category values are mocked (matches size: {}): {}",
          mockedCategoryValues.size(), categoriesCount);
    }

    return mockedCategoryValues;
  }

  private CategoryValue createCategoryValue(MatchCategoryRequest mcr) {
    return CategoryValue.newBuilder()
        .setName(mcr.getCategory())
        .setMatch(mcr.getMatch())
        .setSingleValue(NO_DATA_VALUE)
        .build();
  }

  private boolean categoryValuesContains(
      Collection<CategoryValue> categoryValues, MatchCategoryRequest matchCategoryRequest) {

    var checkIfAnyCategoryValueIsMatchedWithRequest =
        getCategoryValuePredicate(matchCategoryRequest);

    return categoryValues.stream()
        .anyMatch(checkIfAnyCategoryValueIsMatchedWithRequest);
  }

  private Predicate<CategoryValue> getCategoryValuePredicate(
      MatchCategoryRequest matchCategoryRequest) {
    return categoryValue -> categoryValue.getMatch().equals(matchCategoryRequest.getMatch()) &&
        categoryValue.getName().startsWith(matchCategoryRequest.getCategory());
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
