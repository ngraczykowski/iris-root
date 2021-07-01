package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;
import com.silenteight.hsbc.datasource.extractors.document.DocumentExtractor;
import com.silenteight.hsbc.datasource.feature.country.NationalityCountryQuery;

import one.util.streamex.StreamEx;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class NationalityCountryQueryFacade implements NationalityCountryQuery {

  private final IndividualComposite individualComposite;

  @Override
  public Stream<String> apLine4DocumentCountry() {
    var documentFieldWithPossibleCountry = individualComposite.getCustomerIndividual()
        .getIdentificationDocument4();

    return new IdentificationDocumentLine4CountryExtractor(documentFieldWithPossibleCountry)
        .extract()
        .stream();
  }

  @Override
  public Stream<String> apFieldsIndividualCountries() {
    return new CustomerIndividualCountriesExtractor(individualComposite.getCustomerIndividual())
        .extract();
  }

  @Override
  public Stream<String> mpDocumentCountries() {
    return new DocumentExtractor().convertMatchedPartyDocumentNumbers(individualComposite)
        .getAllCountries().stream();
  }

  @Override
  public Stream<String> mpWorldCheckIndividualCountries() {
    return individualComposite.getWorldCheckIndividuals().stream()
        .map(WorldCheckIndividualsCountriesExtractor::new)
        .flatMap(WorldCheckIndividualsCountriesExtractor::extract);
  }

  @Override
  public Stream<String> mpPrivateListIndividualsCountries() {
    return individualComposite.getPrivateListIndividuals().stream()
        .map(PrivateListIndividualsCountriesExtractor::new)
        .flatMap(PrivateListIndividualsCountriesExtractor::extract);
  }

  @Override
  public Stream<String> mpCtrpScreeningIndividualsCountries() {
    return individualComposite.getCtrpScreeningIndividuals().stream()
        .map(CtrpScreeningIndividualsCountriesExtractor::new)
        .flatMap(CtrpScreeningIndividualsCountriesExtractor::extract);
  }

  @Override
  public Stream<String> getWatchlistIndividualsNationalityCountry() {
    var mpWorldCheckCountries = mpWorldCheckIndividualCountries();
    var mpPrivateListIndividualsCountries = mpPrivateListIndividualsCountries();
    var mpCtrpScreeningIndividualsCountries = mpCtrpScreeningIndividualsCountries();

    return StreamEx.of(mpWorldCheckCountries)
        .append(mpPrivateListIndividualsCountries)
        .append(mpCtrpScreeningIndividualsCountries);
  }
}
