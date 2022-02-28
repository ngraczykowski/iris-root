package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.CategoryValue;

@Value
@Builder
public class CategoryValueIn {

  String alert;
  String match;
  String singleValue;
  MultiValueIn multiValue;

  CategoryValue toCategoryValue() {
    CategoryValue.Builder builder = CategoryValue.newBuilder()
        .setAlert(alert)
        .setMatch(match);

    if (singleValue != null) {
      builder
          .setSingleValue(singleValue);
    }
    if (multiValue != null) {
      builder
          .setMultiValue(multiValue.toMultiValue());
    }
    return builder.build();
  }
}
