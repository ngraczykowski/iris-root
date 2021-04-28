package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.domain.IndividualComposite;
import com.silenteight.hsbc.bridge.domain.WorldCheckIndividuals;
import com.silenteight.hsbc.datasource.feature.location.LocationFeatureQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
class LocationFeatureQueryFacade implements LocationFeatureQuery {

  private final IndividualComposite individualComposite;

  @Override
  public Stream<String> worldCheckIndividualsResidencies() {
    return individualComposite.getWorldCheckIndividuals()
        .stream()
        .flatMap(LocationFeatureQueryFacade::extractWorldCheckResidencies);
  }

  private static Stream<String> extractWorldCheckResidencies(
      WorldCheckIndividuals worldCheckIndividuals) {
    return Stream.of(
        worldCheckIndividuals.getAddressCountry(),
        worldCheckIndividuals.getResidencyCountry()
    );
  }

  @Override
  public Stream<String> customerIndividualResidencies() {
    var customerIndividuals = individualComposite.getCustomerIndividuals();

    return Stream.of(
        customerIndividuals.getResidenceCountries(),
        customerIndividuals.getAddressCountry(),
        customerIndividuals.getCountryOfResidence(),
        customerIndividuals.getEdqResidenceCountriesCode(),
        customerIndividuals.getSourceCountry()
    );
  }
}
