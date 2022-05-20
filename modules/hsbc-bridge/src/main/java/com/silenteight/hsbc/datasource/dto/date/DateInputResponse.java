package com.silenteight.hsbc.datasource.dto.date;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class DateInputResponse {

  @Builder.Default
  List<DateInputDto> inputs = Collections.emptyList();
}
