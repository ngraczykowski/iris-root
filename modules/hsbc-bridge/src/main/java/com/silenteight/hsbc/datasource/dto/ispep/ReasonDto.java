package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.Collection;

import static java.util.Collections.emptyList;

@Builder
@Value
public class ReasonDto {

  String message;
  @Builder.Default
  Collection<String> noPepPositions = emptyList();
  @Builder.Default
  Collection<String> notMatchedPositions = emptyList();
  @Builder.Default
  Collection<String> pepPositions = emptyList();
  @Builder.Default
  Collection<String> linkedPepsUids = emptyList();
  int numberOfNotPepDecisions;
  int numberOfPepDecisions;
  String version;
  String regionName;
}
