/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.analysis.categoryrequest.CategoryValuesClient;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryMatches;
import com.silenteight.datasource.categories.api.v2.CategoryValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class CategoryValuesClientMock implements CategoryValuesClient {

  private List<CategoryMatches> categoryMatches = new ArrayList<>();

  @Override
  public BatchGetMatchesCategoryValuesResponse batchGetMatchCategoryValues(
      BatchGetMatchesCategoryValuesRequest request) {
    var mockedResponseBuilder = BatchGetMatchesCategoryValuesResponse.newBuilder();

    request.getCategoryMatchesList().stream()
        .flatMap(this::buildResponse)
        .forEach(mockedResponseBuilder::addCategoryValues);

    return mockedResponseBuilder.build();
  }

  private Stream<CategoryValue> buildResponse(CategoryMatches categoryMatch) {

    var category = categoryMatch.getCategory();

    return categoryMatch.getMatchesList().stream()
        .map(
            m ->
                CategoryValue.newBuilder()
                    .setName(category)
                    .setMatch(m + "/values/123")
                    .setSingleValue("RANDOM_VALUE")
                    .build());
  }
}
