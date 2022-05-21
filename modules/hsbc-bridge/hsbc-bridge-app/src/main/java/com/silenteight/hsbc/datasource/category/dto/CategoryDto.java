package com.silenteight.hsbc.datasource.category.dto;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.category.CategoryType;

import java.util.List;

@Builder
@Value
public class CategoryDto {

  String name;
  String displayName;
  CategoryType categoryType;
  List<String> allowedValues;
  boolean multiValue;
}
