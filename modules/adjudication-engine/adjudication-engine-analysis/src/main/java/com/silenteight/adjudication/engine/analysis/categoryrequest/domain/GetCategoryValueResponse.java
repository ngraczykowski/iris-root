package com.silenteight.adjudication.engine.analysis.categoryrequest.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GetCategoryValueResponse {

  String categoryName;

  String matchName;

  String singleValue;

  boolean multiValue;

  List<String> multiValues;

  public static GetCategoryValueResponse fromCategoryValueV2(
      com.silenteight.datasource.categories.api.v2.CategoryValue categoryValue) {
    return GetCategoryValueResponse
        .builder()
        .categoryName(categoryValue.getName())
        .matchName(categoryValue.getMatch())
        .singleValue(categoryValue.getSingleValue())
        .multiValue(categoryValue.hasMultiValue())
        .multiValues(categoryValue.getMultiValue().getValuesList())
        .build();
  }

  public static GetCategoryValueResponse fromCategoryValueV1(
      com.silenteight.datasource.categories.api.v1.CategoryValue categoryValue) {
    return GetCategoryValueResponse
        .builder()
        .categoryName(categoryValue.getName())
        .matchName(categoryValue.getName())
        .singleValue(categoryValue.getSingleValue())
        .multiValue(categoryValue.hasMultiValue())
        .multiValues(categoryValue.getMultiValue().getValuesList())
        .build();
  }
}
