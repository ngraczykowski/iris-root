package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.country.OtherCountryQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class OtherCountryQueryFacade implements OtherCountryQuery {

  private final MatchData matchData;

  @Override
  public Stream<String> apCustomerIndividualOtherCountries() {
    return matchData.getCustomerIndividuals().stream()
        .flatMap(customerIndividual -> new CustomerIndividualOtherCountriesExtractor(customerIndividual).extract());
  }

  @Override
  public Stream<String> apCustomerEntityOtherCountries() {
    return matchData.getCustomerEntities().stream()
        .flatMap(customerEntity -> new CustomerEntityOtherCountriesExtractor(customerEntity).extract());
  }

  @Override
  public Stream<String> mpWorldCheckIndividualsOtherCountries() {
    return new WorldCheckIndividualsOtherCountriesExtractor(matchData.getWorldCheckIndividuals())
        .extract();
  }

  @Override
  public Stream<String> nnsIndividualsOtherCountries() {
    return new NnsIndividualsOtherCountriesExtractor(matchData.getNnsIndividuals())
        .extract();
  }

  @Override
  public Stream<String> mpWorldCheckEntitiesOtherCountries() {
    return new WorldCheckEntitiesOtherCountriesExtractor(matchData.getWorldCheckEntities())
        .extract();
  }

  @Override
  public Stream<String> mpNnsEntitiesOtherCountries() {
    return new NnsEntitiesOtherCountriesExtractor(matchData.getNnsEntities())
        .extract();
  }

  @Override
  public Stream<String> mpPrivateListIndividualsOtherCountries() {
    return new PrivateListIndividualsOtherCountriesExtractor(matchData.getPrivateListIndividuals())
        .extract();
  }

  @Override
  public Stream<String> mpPrivateListEntitiesOtherCountries() {
    return new PrivateListEntitiesOtherCountriesExtractor(matchData.getPrivateListEntities())
        .extract();
  }

  @Override
  public Stream<String> mpCtrpScreeningIndividualsOtherCountries() {
    return new CtrpScreeningIndividualsOtherCountriesExtractor(
        matchData.getCtrpScreeningIndividuals())
        .extract();
  }

  @Override
  public Stream<String> mpCtrpScreeningEntitiesOtherCountries() {
    return new CtrpScreeningEntitiesOtherCountriesExtractor(matchData.getCtrpScreeningEntities())
        .extract();
  }
}
