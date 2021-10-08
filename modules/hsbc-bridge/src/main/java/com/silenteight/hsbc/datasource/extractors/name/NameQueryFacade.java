package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.name.NameQuery;

import java.util.Collection;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.applyOriginalScriptEnhancements;
import static one.util.streamex.StreamEx.of;

@RequiredArgsConstructor
class NameQueryFacade implements NameQuery {

  private final MatchData matchData;
  private final NameInformationServiceClient nameInformationServiceClient;

  @Override
  public Stream<String> apIndividualExtractProfileFullName() {
    return matchData.getCustomerIndividuals().stream()
        .flatMap(customerIndividual -> Stream.of(customerIndividual.getProfileFullName()))
        .flatMap(name -> new CustomerIndividualOriginalScriptAliasesNameExtractor(name).extract());
  }

  @Override
  public Stream<String> apIndividualExtractNames() {
    return matchData.getCustomerIndividuals().stream()
        .flatMap(customerIndividual -> new CustomerIndividualNamesExtractor(
            customerIndividual).extract());
  }

  @Override
  public Stream<String> apIndividualExtractOtherNames() {
    return matchData.getCustomerIndividuals().stream()
        .flatMap(customerIndividual -> new CustomerIndividualOtherNamesExtractor(
            customerIndividual).extract());
  }

  @Override
  public Stream<String> apEntityExtractEntityNameOriginal() {
    return matchData.getCustomerEntities().stream()
        .flatMap(customerEntity -> Stream.of(customerEntity.getEntityNameOriginal()))
        .flatMap(name -> new CustomerEntityOriginalScriptAliasesNameExtractor(name).extract());
  }

  @Override
  public Stream<String> apEntityExtractOtherNames() {
    return matchData.getCustomerEntities().stream()
        .flatMap(customerEntity -> new CustomerEntityOtherNamesExtractor(customerEntity).extract());
  }

  @Override
  public Stream<String> mpWorldCheckIndividualsExtractNames() {
    var worldCheckIndividuals = matchData.getWorldCheckIndividuals();
    return new WorldCheckIndividualsNamesExtractor(worldCheckIndividuals)
        .extract();
  }

  @Override
  public Stream<String> mpWorldCheckIndividualsExtractXmlNamesWithCountries() {
    return new WorldCheckIndividualsXmlNamesAndCountriesExtractor(
        matchData, nameInformationServiceClient)
        .extract();
  }

  @Override
  public Stream<String> mpWorldCheckEntitiesExtractNames() {
    var worldCheckEntities = matchData.getWorldCheckEntities();
    return new WorldCheckEntitiesNamesExtractor(worldCheckEntities)
        .extract();
  }

  @Override
  public Stream<String> mpWorldCheckEntitiesExtractXmlNamesWithCountries() {
    return new WorldCheckEntitiesXmlNamesAndCountriesExtractor(
        matchData, nameInformationServiceClient)
        .extract();
  }

  @Override
  public Stream<String> mpPrivateListIndividualsExtractNames() {
    var privateListIndividuals = matchData.getPrivateListIndividuals();
    return new PrivateListIndividualsNamesExtractor(privateListIndividuals)
        .extract();
  }

  @Override
  public Stream<String> mpPrivateListEntitiesExtractNames() {
    var privateListEntities = matchData.getPrivateListEntities();
    return new PrivateListEntitiesNamesExtractor(privateListEntities)
        .extract();
  }

  @Override
  public Collection<String> applyOriginalScriptEnhancementsForIndividualNamesWithAliases() {
    return applyOriginalScriptEnhancements(
        apAllIndividualNames(), mpWorldCheckIndividualsExtractXmlNamesWithCountries())
        .getWatchlistPartyIndividuals();
  }

  @Override
  public Party applyOriginalScriptEnhancementsForIndividualNames() {
    return applyOriginalScriptEnhancements(
        apAllIndividualNames(), mpAllIndividualNamesWithoutXmlAliases());
  }

  private Stream<String> apAllIndividualNames() {
    return of(apIndividualExtractProfileFullName())
        .append(apIndividualExtractNames())
        .append(apIndividualExtractOtherNames());
  }

  private Stream<String> mpAllIndividualNamesWithoutXmlAliases() {
    return of(mpWorldCheckIndividualsExtractNames())
        .append(mpPrivateListIndividualsExtractNames());
  }
}
