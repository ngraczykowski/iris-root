package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.CreatedCategoryValue;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class CreatedCategoryValueOut {

  String name;
  String match;

  static CreatedCategoryValueOut createFrom(CreatedCategoryValue value) {
    return CreatedCategoryValueOut.builder()
        .name(value.getName())
        .match(value.getMatch())
        .build();
  }
}
