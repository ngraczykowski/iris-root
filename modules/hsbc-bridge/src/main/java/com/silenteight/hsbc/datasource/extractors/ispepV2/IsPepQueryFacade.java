package com.silenteight.hsbc.datasource.extractors.ispepV2;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.ispep.IsPepQueryV2;

import java.util.stream.Stream;

import static java.util.stream.Stream.empty;

@RequiredArgsConstructor
class IsPepQueryFacade implements IsPepQueryV2 {

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
           new WorldCheckIndividualsLinkedToExtractor(matchData.getWorldCheckIndividuals()).extract() : empty();
  }

  @Override
  public String mpWorldCheckEntitiesFurtherInformation() {
    return matchData.hasWorldCheckEntities() ?
           new WorldCheckEntitiesFurtherInformationExtractor(matchData.getWorldCheckEntities()).extract() : "";
  }

  @Override
  public Stream<String> mpWorldCheckEntitiesLinkedTo() {
    return matchData.hasWorldCheckEntities() ?
           new WorldCheckEntitiesLinkedToExtractor(matchData.getWorldCheckEntities()).extract() : empty();
  }
}
