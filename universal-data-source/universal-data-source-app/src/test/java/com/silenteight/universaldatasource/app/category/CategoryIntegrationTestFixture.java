package com.silenteight.universaldatasource.app.category;

import com.silenteight.datasource.categories.api.v2.*;

import java.util.List;

class CategoryIntegrationTestFixture {

  static BatchGetMatchesCategoryValuesRequest getBatchCategoryValueRequest() {

    var categoryMatch = CategoryMatches.newBuilder()
        .setCategory("categories/categoryThree")
        .addAllMatches(
            List.of("alerts/1/matches/1", "alerts/2/matches/2"))
        .build();

    return BatchGetMatchesCategoryValuesRequest.newBuilder()
        .addAllCategoryMatches(List.of(categoryMatch))
        .build();
  }

  static CreateCategoryValuesRequest getCreateCategoryValuesRequest(String category) {

    var categoryValueOne = CategoryValue.newBuilder()
        .setAlert("alerts/1")
        .setMatch("alerts/1/matches/1")
        .setSingleValue("YES")
        .build();

    var categoryValueTwo = CategoryValue.newBuilder()
        .setAlert("alerts/2")
        .setMatch("alerts/2/matches/2")
        .setSingleValue("NO")
        .build();

    var categoryValueThree = CategoryValue.newBuilder()
        .setAlert("alerts/2")
        .setMatch("alerts/2/matches/3")
        .setSingleValue("YES")
        .build();

    return CreateCategoryValuesRequest.newBuilder()
        .setCategory("categories/" + category)
        .addAllCategoryValues(List.of(categoryValueOne, categoryValueTwo, categoryValueThree))
        .build();
  }

  static BatchCreateCategoryValuesRequest getBatchCreateCategoryValuesRequest(String category) {
    return BatchCreateCategoryValuesRequest.newBuilder()
        .addAllRequests(List.of(
            getCreateCategoryValuesRequest(category)))
        .build();
  }
}
