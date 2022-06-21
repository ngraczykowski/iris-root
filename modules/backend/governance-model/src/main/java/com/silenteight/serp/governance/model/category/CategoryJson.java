package com.silenteight.serp.governance.model.category;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
class CategoryJson {

  @NonNull
  String name;
  @NonNull
  CategoryType type;
  @NonNull
  List<String> values;

  CategoryDto toDto() {
    return CategoryDto.builder()
        .name(this.getName())
        .type(this.getType())
        .values(this.getValues())
        .build();
  }
}
