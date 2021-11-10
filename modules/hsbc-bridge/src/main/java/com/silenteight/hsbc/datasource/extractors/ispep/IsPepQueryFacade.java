package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.ispep.IsPepQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
class IsPepQueryFacade implements IsPepQuery {

  private final MatchData matchData;

  @Override
  public String apWorldCheckIndividualsExtractEdqLobCountryCode() {
    return new CustomerIndividualEdqLobCountryCodeExtractor(matchData.getCustomerIndividuals()).extract();
  }

  @Override
  public String apWorldCheckEntitiesExtractEdqLobCountryCode() {
    return new CustomerEntityEdqLobCountryCodeExtractor(matchData.getCustomerEntities()).extract();
  }

  @Override
  public String mpWorldCheckIndividualsFurtherInformation() {
    return matchData.hasWorldCheckIndividuals() ?
           new WorldCheckIndividualsFurtherInformationExtractor(matchData.getWorldCheckIndividuals()).extract() : "";
  }

  @Override
  public Stream<String> mpWorldCheckIndividualsLinkedTo() {
    return matchData.hasWorldCheckIndividuals() ?
           new WorldCheckIndividualsLinkedToExtractor(matchData.getWorldCheckIndividuals()).extract() : Stream.empty();
  }

  @Override
  public Stream<String> mpWorldCheckIndividualsCountryCodes() {
    return matchData.hasWorldCheckIndividuals() ?
           new WorldCheckIndividualsCountryCodesExtractor(
               matchData.getWorldCheckIndividuals()).extract() : Stream.empty();
  }

  @Override
  public String mpWorldCheckEntitiesFurtherInformation() {
    return matchData.hasWorldCheckEntities() ?
           new WorldCheckEntitiesFurtherInformationExtractor(matchData.getWorldCheckEntities()).extract() : "";
  }

  @Override
  public Stream<String> mpWorldCheckEntitiesLinkedTo() {
    return matchData.hasWorldCheckEntities() ?
           new WorldCheckEntitiesLinkedToExtractor(matchData.getWorldCheckEntities()).extract() : Stream.empty();
  }

  @Override
  public Stream<String> mpWorldCheckEntitiesCountryCodes() {
    return matchData.hasWorldCheckEntities() ?
           new WorldCheckEntitiesCountryCodesExtractor(matchData.getWorldCheckEntities()).extract() : Stream.empty();
  }
}
