package com.silenteight.universaldatasource.api.library.historicaldecisions.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class HistoricalDecisionsInputOut {

  String match;

  @Builder.Default
  List<HistoricalDecisionsFeatureInputOut> featureInputs = List.of();

  static HistoricalDecisionsInputOut createFrom(HistoricalDecisionsInput input) {
    return HistoricalDecisionsInputOut.builder()
        .match(input.getMatch())
        .featureInputs(input.getFeatureInputsList().stream()
            .map(HistoricalDecisionsFeatureInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
