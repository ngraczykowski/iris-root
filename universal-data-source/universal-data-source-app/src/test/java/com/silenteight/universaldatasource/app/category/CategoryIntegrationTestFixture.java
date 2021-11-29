package com.silenteight.universaldatasource.app.category;

import com.silenteight.datasource.categories.api.v2.*;

import java.util.List;

class CategoryIntegrationTestFixture {

  static BatchCreateCategoriesRequest getBatchCategories() {

    var categoryOne = Category.newBuilder()
        .setName("categories/categoryOne")
        .setDisplayName("displayNameOne")
        .setType(CategoryType.ANY_STRING)
        .addAllAllowedValues(List.of("YES", "NO", "MAYBE"))
        .setMultiValue(false)
        .build();

    var categoryTwo = Category.newBuilder()
        .setName("categories/categoryTwo")
        .setDisplayName("displayNameTwo")
        .setType(CategoryType.ENUMERATED)
        .addAllAllowedValues(List.of("YES", "NO"))
        .setMultiValue(false)
        .build();

    var categoryThree = Category.newBuilder()
        .setName("categories/categoryThree")
        .setDisplayName("displayNameThree")
        .setType(CategoryType.ENUMERATED)
        .addAllAllowedValues(List.of("YES", "NO"))
        .setMultiValue(false)
        .build();

    return BatchCreateCategoriesRequest.newBuilder()
        .addAllCategories(List.of(categoryOne, categoryTwo, categoryThree))
        .build();
  }

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
