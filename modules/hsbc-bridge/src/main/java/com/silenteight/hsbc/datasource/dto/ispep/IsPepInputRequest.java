package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Value
public class IsPepInputRequest {

  List<String> matches;
  List<RegionModelFieldDto> regionModelFields;

  public List<Long> getMatchIds() {
    return matches.stream()
        .map(Long::parseLong)
        .collect(Collectors.toList());
  }
}
