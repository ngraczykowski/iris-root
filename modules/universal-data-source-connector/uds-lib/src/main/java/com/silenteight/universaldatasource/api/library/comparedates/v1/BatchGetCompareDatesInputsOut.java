package com.silenteight.universaldatasource.api.library.comparedates.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.compareDates.v1.BatchGetCompareDatesInputsResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetCompareDatesInputsOut {

  @Builder.Default
  List<CompareDatesInputOut> documentInputs = List.of();

  static BatchGetCompareDatesInputsOut createFrom(BatchGetCompareDatesInputsResponse response) {
    return BatchGetCompareDatesInputsOut
        .builder()
        .documentInputs(response
            .getCompareDatesInputsList()
            .stream()
            .map(CompareDatesInputOut::createFrom)
            .collect(toList()))
        .build();
  }
}
