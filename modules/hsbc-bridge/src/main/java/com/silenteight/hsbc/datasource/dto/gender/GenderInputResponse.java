package com.silenteight.hsbc.datasource.dto.gender;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class GenderInputResponse {

  List<GenderInputDto> inputs;
}
