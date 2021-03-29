package com.silenteight.hsbc.datasource.category;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import javax.validation.constraints.NotNull;

@Builder
@Value
class CategoryModel {

  @NonNull String name;
  String displayName;
  boolean multiValue;
  @NonNull CategoryType type;
  @NonNull List<String> allowedValues;
  @NonNull CategoryValueRetriever valueRetriever;
}
