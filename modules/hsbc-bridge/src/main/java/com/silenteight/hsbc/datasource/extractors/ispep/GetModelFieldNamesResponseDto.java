package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class GetModelFieldNamesResponseDto {

  @Builder.Default
  List<String> fieldNames = emptyList();
  @NonNull
  String version;
  @NonNull
  String regionName;
}
