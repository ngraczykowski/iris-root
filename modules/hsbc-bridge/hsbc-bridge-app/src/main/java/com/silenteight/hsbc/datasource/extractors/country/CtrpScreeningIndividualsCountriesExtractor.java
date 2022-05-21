package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@RequiredArgsConstructor
class CtrpScreeningIndividualsCountriesExtractor {

  private final CtrpScreening ctrpScreening;

  public Stream<String> extract() {
    return Stream.of(
        ctrpScreening.getCountryName(),
        ctrpScreening.getCountryCode(),
        ctrpScreening.getCtrpValue()
    ).filter(StringUtils::isNotBlank);
  }
}
