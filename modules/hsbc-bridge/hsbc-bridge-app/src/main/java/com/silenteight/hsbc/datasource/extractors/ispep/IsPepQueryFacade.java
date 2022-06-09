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
    return new CustomerIndividualEdqLobCountryCodeExtractor(matchData.getCustomerIndividuals())
        .extract();
  }

  @Override
  public String apWorldCheckEntitiesExtractEdqLobCountryCode() {
    return new CustomerEntityEdqLobCountryCodeExtractor(matchData.getCustomerEntities()).extract();
  }

  @Override
  public String individualsFurtherInformation() {
    if (matchData.hasWorldCheckIndividuals()) {
      return new WorldCheckIndividualsFurtherInformationExtractor(
              matchData.getWorldCheckIndividuals())
          .extract();
    } else if (matchData.hasNnsIndividuals()) {
      return new NnsIndividualsFurtherInformationExtractor(matchData.getNnsIndividuals()).extract();
    }

    return "";
  }

  @Override
  public Stream<String> individualsLinkedTo() {
    if (matchData.hasWorldCheckIndividuals()) {
      return new WorldCheckIndividualsLinkedToExtractor(matchData.getWorldCheckIndividuals())
          .extract();
    } else if (matchData.hasNnsIndividuals()) {
      return new NnsIndividualsLinkedToExtractor(matchData.getNnsIndividuals()).extract();
    }

    return Stream.empty();
  }

  @Override
  public Stream<String> individualsCountryCodes() {
    if (matchData.hasWorldCheckIndividuals()) {
      return new WorldCheckIndividualsCountryCodesExtractor(matchData.getWorldCheckIndividuals())
          .extract();
    } else if (matchData.hasNnsIndividuals()) {
      return new NnsIndividualsCountryCodesExtractor(matchData.getNnsIndividuals()).extract();
    }

    return Stream.empty();
  }

  @Override
  public String entitiesFurtherInformation() {
    if (matchData.hasWorldCheckEntities()) {
      return new WorldCheckEntitiesFurtherInformationExtractor(matchData.getWorldCheckEntities())
          .extract();
    } else if (matchData.hasNnsEntities()) {
      return new NnsEntitiesFurtherInformationExtractor(matchData.getNnsEntities()).extract();
    }

    return "";
  }

  @Override
  public Stream<String> entitiesLinkedTo() {
    if (matchData.hasWorldCheckEntities()) {
      return new WorldCheckEntitiesLinkedToExtractor(matchData.getWorldCheckEntities()).extract();
    } else if (matchData.hasNnsEntities()) {
      return new NnsEntitiesLinkedToExtractor(matchData.getNnsEntities()).extract();
    }

    return Stream.empty();
  }

  @Override
  public Stream<String> entitiesCountryCodes() {
    if (matchData.hasWorldCheckEntities()) {
      return new WorldCheckEntitiesCountryCodesExtractor(matchData.getWorldCheckEntities())
          .extract();
    } else if (matchData.hasNnsEntities()) {
      return new NnsEntitiesCountryCodesExtractor(matchData.getNnsEntities()).extract();
    }
    return Stream.empty();
  }
}
