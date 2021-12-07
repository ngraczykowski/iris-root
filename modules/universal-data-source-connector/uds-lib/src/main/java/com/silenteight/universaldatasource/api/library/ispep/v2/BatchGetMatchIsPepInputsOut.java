package com.silenteight.universaldatasource.api.library.ispep.v2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchIsPepInputsOut {

  @Builder.Default
  List<IsPepInputOut> isPepInputs = List.of();

  static BatchGetMatchIsPepInputsOut createFrom(BatchGetMatchIsPepInputsResponse response) {
    return BatchGetMatchIsPepInputsOut.builder()
        .isPepInputs(response.getIsPepInputsList().stream()
            .map(IsPepInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
