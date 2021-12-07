package com.silenteight.universaldatasource.api.library.date.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.date.v1.DateInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class DateInputOut {

  @Builder.Default
  List<DateFeatureInputOut> dateInputs = List.of();

  static DateInputOut createFrom(DateInput input) {
    return DateInputOut.builder()
        .dateInputs(input.getDateFeatureInputsList()
            .stream()
            .map(DateFeatureInputOut::createFrom)
            .collect(Collectors.toList())
        )
        .build();
  }
}
