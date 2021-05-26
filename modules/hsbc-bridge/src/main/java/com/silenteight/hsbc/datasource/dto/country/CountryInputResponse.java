package com.silenteight.hsbc.datasource.dto.country;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@Builder
public class CountryInputResponse {

  @Builder.Default
  List<CountryInputDto> inputs = emptyList();
}
