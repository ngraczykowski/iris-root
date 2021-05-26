package com.silenteight.hsbc.datasource.dto.date;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@Builder
public class DateInputResponse {

  @Builder.Default
  List<DateInputDto> inputs = emptyList();
}
