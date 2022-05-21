package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DiscriminatorDto {

  String value;
}
