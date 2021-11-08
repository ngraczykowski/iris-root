package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Builder
@Value
public class IsPepRequestDto {

  @Builder.Default
  List<String> fieldNames = Collections.emptyList();
  @Builder.Default
  Map<String, String> apFields = Collections.emptyMap();
  @NonNull
  String bankRegion;
  @NonNull
  String uid;
}
