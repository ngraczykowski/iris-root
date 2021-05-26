package com.silenteight.hsbc.datasource.dto.location;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class LocationInputResponse {

  @Builder.Default
  List<LocationInputDto> inputs = emptyList();
}
