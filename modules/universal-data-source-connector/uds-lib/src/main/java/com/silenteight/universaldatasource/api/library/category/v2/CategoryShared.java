package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.Category;

import java.util.List;

@Value
@Builder
public class CategoryShared {

  String name;
  String displayName;
  CategoryTypeShared categoryType;
  List<String> allowedValues;
  boolean multiValue;

  Category toCategoryProto() {
    return Category.newBuilder()
        .setName(name)
        .setDisplayName(displayName)
        .setType(CategoryTypeShared.toCategoryTypeProto(categoryType))
        .addAllAllowedValues(allowedValues)
        .setMultiValue(multiValue)
        .build();
  }

  static CategoryShared toCategoryShared(
      Category category) {
    return CategoryShared.builder()
        .name(category.getName())
        .displayName(category.getDisplayName())
        .categoryType(CategoryTypeShared.toCategoryTypeShared(category.getType()))
        .allowedValues(category.getAllowedValuesList())
        .multiValue(category.getMultiValue())
        .build();
  }
}
