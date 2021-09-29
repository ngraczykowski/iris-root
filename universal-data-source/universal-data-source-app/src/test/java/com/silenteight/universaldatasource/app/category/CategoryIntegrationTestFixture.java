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
        .setCategories("categories/categoryThree")
        .addAllMatches(
            List.of("alerts/alertOne/matches/matchOne", "alerts/alertTwo/matches/matchTwo"))
        .build();

    return BatchGetMatchesCategoryValuesRequest.newBuilder()
        .addAllCategoryMatches(List.of(categoryMatch))
        .build();
  }

  static CreateCategoryValuesRequest getCreateCategoryValuesRequest(String category) {

    var categoryValueOne = CategoryValue.newBuilder()
        .setMatch("alerts/alertOne/matches/matchOne")
        .setSingleValue("YES")
        .build();

    var categoryValueTwo = CategoryValue.newBuilder()
        .setMatch("alerts/alertTwo/matches/matchTwo")
        .setSingleValue("NO")
        .build();

    var categoryValueThree = CategoryValue.newBuilder()
        .setMatch("alerts/alertTwo/matches/matchThree")
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
