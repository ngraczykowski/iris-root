package com.silenteight.universaldatasource.api.library.date.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.date.v1.BatchGetMatchDateInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchDateInputsOut {

  @Builder.Default
  List<DateInputOut> dateInputs = List.of();

  static BatchGetMatchDateInputsOut createFrom(BatchGetMatchDateInputsResponse response) {
    return BatchGetMatchDateInputsOut.builder()
        .dateInputs(response.getDateInputsList()
            .stream()
            .map(DateInputOut::createFrom)
            .collect(Collectors.toList())
        )
        .build();
  }
}
