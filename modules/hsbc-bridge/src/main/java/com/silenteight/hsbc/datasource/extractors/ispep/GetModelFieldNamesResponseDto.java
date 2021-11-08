package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class GetModelFieldNamesResponseDto {

  @Builder.Default
  List<String> fieldNames = Collections.emptyList();
  @NonNull
  String version;
  @NonNull
  String regionName;
}
