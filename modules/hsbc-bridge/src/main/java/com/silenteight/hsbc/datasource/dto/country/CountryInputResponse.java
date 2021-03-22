package com.silenteight.hsbc.datasource.dto.country;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CountryInputResponse {

  List<CountryInputDto> inputs;
}
