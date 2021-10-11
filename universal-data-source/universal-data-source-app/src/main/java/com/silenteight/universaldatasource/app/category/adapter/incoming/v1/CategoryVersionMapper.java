package com.silenteight.universaldatasource.app.category.adapter.incoming.v1;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.Category;
import com.silenteight.datasource.categories.api.v1.CategoryType;
import com.silenteight.datasource.categories.api.v1.CategoryValue;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryMatches;
import com.silenteight.universaldatasource.common.resource.ResourceName;

import com.google.protobuf.ProtocolStringList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
class CategoryVersionMapper {

  List<Category> mapCategoryListToV1(
      List<com.silenteight.datasource.categories.api.v2.Category> categoriesList) {
    return categoriesList
        .stream()
        .map(c -> Category.newBuilder()
            .setName(c.getName())
            .setDisplayName(c.getDisplayName())
            .setType(CategoryType.valueOf(c.getType().name()))
            .addAllAllowedValues(c.getAllowedValuesList())
            .setMultiValue(c.getMultiValue())
            .build())
        .collect(Collectors.toList());
  }

  List<CategoryMatches> mapStringListToCategoryMatches(
      ProtocolStringList matchValuesList) {
    var matchValueMap = matchValuesListToMap(matchValuesList);
    return mapToCategoryMatches(matchValueMap);
  }

  BatchGetMatchCategoryValuesResponse mapBatchResponse(
      BatchGetMatchesCategoryValuesResponse batchGetMatchesCategoryValuesResponse) {

    var categoryValuesList =
        batchGetMatchesCategoryValuesResponse.getCategoryValuesList().stream()
            .map(c -> CategoryValue.newBuilder()
                .setName(c.getName())
                .setSingleValue(c.getSingleValue())
                .build())
            .collect(Collectors.toList());

    return BatchGetMatchCategoryValuesResponse.newBuilder()
        .addAllCategoryValues(categoryValuesList)
        .build();
  }

  private static Map<String, List<String>> matchValuesListToMap(
      ProtocolStringList matchValuesList) {
    Map<String, List<String>> matchValueMap = new HashMap<>();

    for (String matchValue : matchValuesList) {

      var resourceName = ResourceName.create(matchValue);
      var category = resourceName.get("categories");
      var alert = resourceName.get("alerts");
      var match = resourceName.get("matches");

      matchValueMap.putIfAbsent("categories/" + category, new ArrayList<>());
      matchValueMap.get("categories/" + category).add("alerts/" + alert + "/matches/" + match);
    }
    return matchValueMap;
  }

  private List<CategoryMatches> mapToCategoryMatches(
      Map<String, List<String>> matchValueMap) {
    List<CategoryMatches> categoryMatches = new ArrayList<>();

    for (Entry<String, List<String>> entry : matchValueMap.entrySet()) {
      var categoryMatch = getCategoryBuilder(entry);
      categoryMatches.add(categoryMatch);
    }
    return categoryMatches;
  }

  private static CategoryMatches getCategoryBuilder(Entry<String, List<String>> entry) {
    var builder = CategoryMatches.newBuilder()
        .setCategory(entry.getKey());
    for (String match : entry.getValue()) {
      builder.addMatches(match);
    }
    return builder.build();
  }
}
