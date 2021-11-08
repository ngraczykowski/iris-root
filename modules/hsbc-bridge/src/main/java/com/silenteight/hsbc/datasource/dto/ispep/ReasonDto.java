package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.Collection;
import java.util.Collections;

@Builder
@Value
public class ReasonDto {

  String message;
  @Builder.Default
  Collection<String> noPepPositions = Collections.emptyList();
  @Builder.Default
  Collection<String> notMatchedPositions = Collections.emptyList();
  @Builder.Default
  Collection<String> pepPositions = Collections.emptyList();
  @Builder.Default
  Collection<String> linkedPepsUids = Collections.emptyList();
  int numberOfNotPepDecisions;
  int numberOfPepDecisions;
  String version;
  String regionName;
}
