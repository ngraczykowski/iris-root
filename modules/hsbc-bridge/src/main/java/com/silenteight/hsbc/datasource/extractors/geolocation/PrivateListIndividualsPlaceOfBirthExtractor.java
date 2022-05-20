package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;
import com.silenteight.hsbc.datasource.extractors.common.SignType;

import one.util.streamex.StreamEx;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class PrivateListIndividualsPlaceOfBirthExtractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  public List<String> extract() {
    return privateListIndividuals.stream()
        .flatMap(PrivateListIndividualsPlaceOfBirthExtractor::extractPrivateListFields)
        .collect(Collectors.toList());
  }

  private static Stream<String> extractPrivateListFields(
      PrivateListIndividual privateListIndividual) {

    var countryOfBirth = Stream.of(privateListIndividual.getCountryOfBirth());

    var placeOfBirth =
        GeoLocationExtractor.splitExtractedValueBySign(SignType.SEMICOLON, privateListIndividual.getPlaceOfBirth());

    return StreamEx.of(countryOfBirth).append(placeOfBirth);
  }
}
