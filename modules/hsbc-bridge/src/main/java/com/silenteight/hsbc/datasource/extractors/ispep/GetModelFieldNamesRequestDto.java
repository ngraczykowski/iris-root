package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.NonNull;
import lombok.Value;

@Value
public class GetModelFieldNamesRequestDto {

  @NonNull
  String bankRegion;
}
