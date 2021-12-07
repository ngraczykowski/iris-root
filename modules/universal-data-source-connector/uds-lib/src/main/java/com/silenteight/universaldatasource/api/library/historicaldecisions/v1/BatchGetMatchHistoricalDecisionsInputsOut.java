package com.silenteight.universaldatasource.api.library.historicaldecisions.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class BatchGetMatchHistoricalDecisionsInputsOut {

  @Builder.Default
  List<HistoricalDecisionsInputOut> historicalDecisionsInputs = List.of();

  static BatchGetMatchHistoricalDecisionsInputsOut createFrom(
      BatchGetMatchHistoricalDecisionsInputsResponse response) {
    return BatchGetMatchHistoricalDecisionsInputsOut.builder()
        .historicalDecisionsInputs(response.getHistoricalDecisionsInputsList()
            .stream()
            .map(HistoricalDecisionsInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
