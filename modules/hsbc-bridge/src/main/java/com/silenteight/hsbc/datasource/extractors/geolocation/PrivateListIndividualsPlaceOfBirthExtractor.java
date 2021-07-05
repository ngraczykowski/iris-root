package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import one.util.streamex.StreamEx;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.SignType.SEMICOLON;
import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.splitExtractedValueBySign;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class PrivateListIndividualsPlaceOfBirthExtractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  public List<String> extract() {
    return privateListIndividuals.stream()
        .flatMap(PrivateListIndividualsPlaceOfBirthExtractor::extractPrivateListFields)
        .collect(toList());
  }

  private static Stream<String> extractPrivateListFields(
      PrivateListIndividual privateListIndividual) {

    var countryOfBirth = of(privateListIndividual.getCountryOfBirth());

    var placeOfBirth =
        splitExtractedValueBySign(SEMICOLON, privateListIndividual.getPlaceOfBirth());

    return StreamEx.of(countryOfBirth).append(placeOfBirth);
  }
}
