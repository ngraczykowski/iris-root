package com.silenteight.serp.governance.model.category;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CategoryDto {

  @NonNull
  String name;
  @NonNull
  CategoryType type;
  @NonNull
  List<String> values;
}
