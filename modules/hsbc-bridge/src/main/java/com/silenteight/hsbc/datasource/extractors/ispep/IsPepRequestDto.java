package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Builder
@Value
public class IsPepRequestDto {

  @Builder.Default
  List<String> fieldNames = emptyList();
  @Builder.Default
  Map<String, String> apFields = emptyMap();
  @NonNull
  String bankRegion;
  @NonNull
  String uid;
}
