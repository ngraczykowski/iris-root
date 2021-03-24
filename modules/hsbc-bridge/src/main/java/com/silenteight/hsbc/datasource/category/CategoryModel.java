package com.silenteight.hsbc.datasource.category;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import javax.validation.constraints.NotNull;

@Builder
@Value
class CategoryModel {

  @NotNull String name;
  String displayName;
  boolean multiValue;
  @NotNull CategoryType type;
  @NotNull List<String> allowedValues;
}
