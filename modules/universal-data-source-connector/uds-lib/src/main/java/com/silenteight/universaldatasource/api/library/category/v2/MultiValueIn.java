package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.MultiValue;

import java.util.List;

@Value
@Builder
public class MultiValueIn {

  @Builder.Default
  List<String> values = List.of();

  MultiValue toMultiValue() {
    return MultiValue.newBuilder()
        .addAllValues(this.values)
        .build();
  }
}
