package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CtrpScreeningExtractor {

  private final List<CtrpScreening> ctrpScreening;

  public List<String> extract() {
    return ctrpScreening.stream()
        .flatMap(
            CtrpScreeningExtractor::extractCtrpScreeningFields)
        .collect(Collectors.toList());
  }

  private static Stream<String> extractCtrpScreeningFields(CtrpScreening ctrpScreeningEntities) {
    return Stream.of(
        ctrpScreeningEntities.getCtrpValue(),
        ctrpScreeningEntities.getCountryCode()
    );
  }
}
