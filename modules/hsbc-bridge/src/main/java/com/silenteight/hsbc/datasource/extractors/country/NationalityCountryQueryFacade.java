package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.domain.IndividualComposite;
import com.silenteight.hsbc.datasource.extractors.document.DocumentExtractor;
import com.silenteight.hsbc.datasource.feature.country.NationalityCountryQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
class NationalityCountryQueryFacade implements NationalityCountryQuery {

  private final IndividualComposite individualComposite;

  @Override
  public Stream<String> apLine4DocumentCountry() {
    var documentFieldWithPossibleCountry = individualComposite.getCustomerIndividuals()
        .getIdentificationDocument4();

    return new IdentificationDocumentLine4CountryExtractor(documentFieldWithPossibleCountry)
        .extract()
        .stream();
  }

  @Override
  public Stream<String> apFieldsIndividualCountries() {
    return new CustomerIndividualCountriesExtractor(individualComposite.getCustomerIndividuals())
        .extract();
  }

  @Override
  public Stream<String> mpDocumentCountries() {
    return new DocumentExtractor().convertMatchedPartyDocumentNumbers(individualComposite)
        .getAllCountries().stream();
  }

  @Override
  public Stream<String> mpWorldCheckCountries() {
    return individualComposite.getWorldCheckIndividuals().stream()
        .map(WorldCheckIndividualsCountriesExtractor::new)
        .flatMap(WorldCheckIndividualsCountriesExtractor::extract);
  }
}
