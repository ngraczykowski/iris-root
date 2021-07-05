package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CtrpScreeningExtractor {

  private final List<CtrpScreening> ctrpScreening;

  public List<String> extract() {
    return ctrpScreening.stream()
        .flatMap(
            CtrpScreeningExtractor::extractCtrpScreeningFields)
        .collect(toList());
  }

  private static Stream<String> extractCtrpScreeningFields(CtrpScreening ctrpScreeningEntities) {
    return of(
        ctrpScreeningEntities.getCtrpValue(),
        ctrpScreeningEntities.getCountryCode()
    );
  }
}
