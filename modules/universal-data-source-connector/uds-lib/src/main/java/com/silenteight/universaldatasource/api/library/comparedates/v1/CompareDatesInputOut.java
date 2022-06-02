package com.silenteight.universaldatasource.api.library.comparedates.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.compareDates.v1.CompareDatesInput;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class CompareDatesInputOut {

  String match;

  @Builder.Default
  List<CompareDatesFeatureInputOut> compareDatesInputs = List.of();

  static CompareDatesInputOut createFrom(CompareDatesInput input) {
    return CompareDatesInputOut
        .builder()
        .match(input.getMatch())
        .compareDatesInputs(input
            .getCompareDatesFeaturesInputList()
            .stream()
            .map(CompareDatesFeatureInputOut::createFrom)
            .collect(toList()))
        .build();
  }
}
