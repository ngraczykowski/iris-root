package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;
import com.silenteight.hsbc.datasource.feature.country.ResidencyCountryFeatureQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
class ResidencyCountryFeatureQueryFacade implements ResidencyCountryFeatureQuery {

  private final IndividualComposite individualComposite;

  @Override
  public Stream<String> worldCheckIndividualsResidencies() {
    return individualComposite.getWorldCheckIndividuals()
        .stream()
        .flatMap(ResidencyCountryFeatureQueryFacade::extractWorldCheckResidencies);
  }

  private static Stream<String> extractWorldCheckResidencies(
      WorldCheckIndividual worldCheckIndividual) {
    return Stream.of(
        worldCheckIndividual.getAddressCountry(),
        worldCheckIndividual.getResidencyCountry()
    );
  }

  @Override
  public Stream<String> customerIndividualResidencies() {
    var customerIndividuals = individualComposite.getCustomerIndividual();

    return Stream.of(
        customerIndividuals.getResidenceCountries(),
        customerIndividuals.getAddressCountry(),
        customerIndividuals.getCountryOfResidence(),
        customerIndividuals.getEdqResidenceCountriesCode(),
        customerIndividuals.getSourceCountry()
    );
  }
}
